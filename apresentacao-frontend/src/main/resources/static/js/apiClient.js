const API_BASE_URL_STORAGE_KEY = "acadtrackApiBaseUrl";
const API_PROBE_PATH = "/v3/api-docs";
const API_CANDIDATE_PORTS = Array.from({ length: 20 }, (_, index) => 8080 + index);

let apiBaseUrlPromise = null;

function normalizeApiBaseUrl(value) {
    return String(value || "").trim().replace(/\/+$/, "");
}

function addUniqueApiBaseUrl(candidates, value) {
    const normalized = normalizeApiBaseUrl(value);
    if (normalized && !candidates.includes(normalized)) {
        candidates.push(normalized);
    }
}

function getStoredApiBaseUrl() {
    try {
        return window.localStorage.getItem(API_BASE_URL_STORAGE_KEY);
    } catch {
        return "";
    }
}

function rememberApiBaseUrl(baseUrl) {
    try {
        if (baseUrl) {
            window.localStorage.setItem(API_BASE_URL_STORAGE_KEY, baseUrl);
        }
    } catch {
        // localStorage can be unavailable for file:// pages in some browsers.
    }
}

function getApiBaseUrlCandidates() {
    const candidates = [];
    const params = new URLSearchParams(window.location.search);

    addUniqueApiBaseUrl(candidates, params.get("api"));
    addUniqueApiBaseUrl(candidates, params.get("apiBaseUrl"));
    addUniqueApiBaseUrl(candidates, window.ACADTRACK_API_BASE_URL);

    if (window.location.protocol === "http:" || window.location.protocol === "https:") {
        addUniqueApiBaseUrl(candidates, window.location.origin);
    }

    addUniqueApiBaseUrl(candidates, getStoredApiBaseUrl());
    API_CANDIDATE_PORTS.forEach((port) => addUniqueApiBaseUrl(candidates, `http://localhost:${port}`));

    return candidates;
}

function isHttpUrl(value) {
    return /^https?:\/\//i.test(String(value || ""));
}

function buildApiUrl(path, baseUrl) {
    if (isHttpUrl(path)) {
        return path;
    }

    const normalizedPath = String(path || "").startsWith("/") ? path : `/${path}`;
    return `${baseUrl}${normalizedPath}`;
}

export async function fetchWithTimeout(url, options = {}, timeoutMs = 12000) {
    const controller = new AbortController();
    const timeoutId = window.setTimeout(() => controller.abort(), timeoutMs);

    try {
        return await fetch(url, {
            ...options,
            signal: controller.signal
        });
    } finally {
        window.clearTimeout(timeoutId);
    }
}

async function canUseApiBaseUrl(baseUrl) {
    try {
        const response = await fetchWithTimeout(buildApiUrl(API_PROBE_PATH, baseUrl), {
            headers: { "Accept": "application/json" }
        }, 1800);

        return response.ok;
    } catch {
        return false;
    }
}

export async function resolveApiBaseUrl() {
    if (!apiBaseUrlPromise) {
        apiBaseUrlPromise = (async () => {
            const candidates = getApiBaseUrlCandidates();

            for (const baseUrl of candidates) {
                if (await canUseApiBaseUrl(baseUrl)) {
                    rememberApiBaseUrl(baseUrl);
                    return baseUrl;
                }
            }

            throw new Error("Não foi possível conectar à API. Inicie o backend Spring Boot e, se ele estiver em outra porta, abra a tela com ?api=http://localhost:PORT.");
        })().catch((error) => {
            apiBaseUrlPromise = null;
            throw error;
        });
    }

    return apiBaseUrlPromise;
}

export async function requestJson(path, options = {}) {
    const apiBaseUrl = isHttpUrl(path) ? "" : await resolveApiBaseUrl();
    const url = buildApiUrl(path, apiBaseUrl);

    let response;
    try {
        response = await fetchWithTimeout(url, {
            ...options,
            headers: {
                "Accept": "application/json",
                ...(options.headers || {})
            }
        });
    } catch {
        apiBaseUrlPromise = null;
        throw new Error("Falha ao conectar à API. Verifique se o backend está em execução e tente novamente.");
    }

    const contentType = response.headers.get("content-type") || "";
    const payload = contentType.includes("application/json") ? await response.json() : await response.text();

    if (!response.ok) {
        const message = typeof payload === "object" && payload !== null && payload.message
            ? payload.message
            : `A API retornou status ${response.status}.`;
        const error = new Error(message);
        error.status = response.status;
        error.payload = payload;
        throw error;
    }

    return payload;
}
