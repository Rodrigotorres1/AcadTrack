const state = {
    perfil: null,
    alunoMatricula: null,
    alunos: [],
    turmas: [],
    disciplinas: [],
    disciplinaNomePorId: {},
    simuladoDescricaoPorId: {},
    notaAlunos: [],
    notaSimulados: [],
    notaDisciplinas: [],
    notaSimuladoSelecionadoId: null,
    performanceAlunos: [],
    simulados: [],
    simuladoDisciplinas: [],
    retificacoes: [],
    selectedRetificacao: null,
    notificationsResponsavelId: null,
    responsaveis: [],
    responsaveisLinhas: []
};

const PROFILE_CONFIG = {
    coordenador: {
        label: "Coordenador",
        description: "Gerencia disciplinas, simulados, turmas e vínculos.",
        defaultSection: "disciplinas"
    },
    professor: {
        label: "Professor",
        description: "Lança notas e participa da avaliação/retificação.",
        defaultSection: "notas"
    },
    aluno: {
        label: "Aluno",
        description: "Consulta de desempenho acadêmico e ranking.",
        defaultSection: "desempenho"
    },
    responsavel: {
        label: "Responsável",
        description: "Portal, permissões e notificações.",
        defaultSection: "portal"
    }
};

const ROLE_SECTIONS = {
    coordenador: ["disciplinas", "simulados", "turmas", "alunos", "responsaveis"],
    professor: ["notas", "desempenho", "retificacoes"],
    aluno: ["notas", "desempenho", "retificacoes"],
    responsavel: ["portal", "notificacoes"]
};

const formatNumber = (value) => {
    if (value === null || value === undefined || Number.isNaN(Number(value))) {
        return "-";
    }
    return Number(value).toLocaleString("pt-BR", {
        minimumFractionDigits: 1,
        maximumFractionDigits: 2
    });
};

const normalize = (value) => String(value || "").toLowerCase();

const normalizeClassName = (value) => String(value || "")
    .trim()
    .toLowerCase()
    .replaceAll("°", "º")
    .replace(/\s+/g, "");

const riskClass = (nivelRisco) => {
    const risk = normalize(nivelRisco);
    if (risk.includes("alto")) return "alto";
    if (risk.includes("moderado")) return "moderado";
    if (risk.includes("baixo")) return "baixo";
    return "neutral";
};

const statusClass = (value) => {
    const text = normalize(value);
    if (text.includes("alto") || text.includes("erro") || text.includes("bloque")) return "danger";
    if (text.includes("moderado") || text.includes("atenc") || text.includes("media")) return "warning";
    if (text.includes("baixo") || text.includes("sucesso") || text.includes("ok")) return "success";
    return "neutral";
};

const escapeHtml = (value) => String(value ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");

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

async function fetchWithTimeout(url, options = {}, timeoutMs = 12000) {
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

async function resolveApiBaseUrl() {
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

async function requestJson(path, options = {}) {
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
        throw error;
    }

    return payload;
}

function setStatus(elementId, text, kind = "neutral") {
    const element = document.getElementById(elementId);
    element.textContent = text;
    element.className = `status-pill ${kind}`;
}

function message(text, kind = "neutral") {
    const className = kind === "error" ? "message-box error" : kind === "ok" ? "message-box ok" : "message-box";
    return `<div class="${className}">${escapeHtml(text)}</div>`;
}

function getProfileConfig(profile) {
    return PROFILE_CONFIG[profile] || null;
}

function getAllowedSections(profile) {
    return ROLE_SECTIONS[profile] || [];
}

function canAccessSection(sectionId) {
    return !state.perfil || getAllowedSections(state.perfil).includes(sectionId);
}

function applyRoleNavigation(profile) {
    const allowedSections = getAllowedSections(profile);
    document.querySelectorAll(".nav-link").forEach((link) => {
        const allowed = allowedSections.includes(link.dataset.section);
        link.hidden = !allowed;
        if (!allowed) {
            link.classList.remove("active");
        }
    });
}

function renderActiveProfile(profile) {
    const config = getProfileConfig(profile);
    document.getElementById("activeProfileLabel").textContent = config?.label || "-";
    document.getElementById("activeProfileDescription").textContent = config?.description || "";
}

function isPositiveEnrollment(value) {
    return /^\d+$/.test(String(value || "").trim()) && Number(value) >= 1;
}

function rememberStudentEnrollment(value) {
    state.alunoMatricula = String(value || "").trim() || null;
}

function getStudentEnrollmentFromForm(form) {
    return String(new FormData(form).get("alunoId") || "").trim();
}

function showLogin(feedback = "") {
    state.perfil = null;
    state.alunoMatricula = null;
    applyRoleNavigation(null);
    document.body.classList.add("login-active");
    document.querySelector(".sidebar").hidden = true;
    document.getElementById("loginScreen").hidden = false;
    document.getElementById("loginFeedback").innerHTML = feedback;
    renderActiveProfile(null);
}

function enterApplication(profile) {
    const config = getProfileConfig(profile);
    if (!config) {
        showLogin(message("Selecione um perfil válido.", "error"));
        return;
    }

    state.perfil = profile;
    state.alunoMatricula = null;
    applyRoleNavigation(profile);
    document.body.classList.remove("login-active");
    document.querySelector(".sidebar").hidden = false;
    document.getElementById("loginScreen").hidden = true;
    renderActiveProfile(profile);
    showSection(config.defaultSection);
}

function handleLoginSubmit(event) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const profile = formData.get("perfil");

    enterApplication(profile);
}

function handleChangeProfile() {
    document.getElementById("loginForm").reset();
    showLogin();
}

function showSection(sectionId) {
    if (!canAccessSection(sectionId)) {
        const fallback = getProfileConfig(state.perfil)?.defaultSection;
        if (fallback && fallback !== sectionId) {
            showSection(fallback);
        }
        return;
    }

    document.querySelectorAll(".content-section").forEach((section) => {
        section.classList.toggle("active", section.id === sectionId);
    });
    document.querySelectorAll(".nav-link").forEach((link) => {
        link.classList.toggle("active", link.dataset.section === sectionId);
    });

    if (sectionId === "alunos") {
        showStudentsList();
        loadStudentsView();
    }

    if (sectionId === "turmas") {
        showClassesList();
        loadClassesView();
    }

    if (sectionId === "disciplinas") {
        showSubjectsList();
        loadSubjects();
    }

    if (sectionId === "notas") {
        loadNotesView();
    }

    if (sectionId === "desempenho") {
        loadPerformanceView();
    }

    if (sectionId === "simulados") {
        showSimulationsList();
        loadSimulationsView();
    }

    if (sectionId === "retificacoes") {
        showCorrectionsList();
        loadCorrectionsView();
    }

    if (sectionId === "responsaveis") {
        showGuardiansList();
        loadGuardiansView();
    }
}

function renderRiskPill(nivelRisco) {
    return `<span class="risk-pill ${riskClass(nivelRisco)}">${escapeHtml(nivelRisco || "-")}</span>`;
}

function showPerformanceSelect(feedback = "") {
    document.getElementById("performanceSelectView").hidden = false;
    document.getElementById("performancePanelView").hidden = true;
    document.getElementById("performanceFeedback").innerHTML = feedback;
}

function renderPerformanceStudentOptions() {
    const select = document.getElementById("performanceStudentSelect");
    const input = document.getElementById("performanceStudentEnrollment");
    const label = document.getElementById("performanceStudentLabel");

    if (state.perfil === "aluno") {
        label.textContent = "Matrícula";
        label.setAttribute("for", "performanceStudentEnrollment");
        select.hidden = true;
        select.disabled = true;
        select.required = false;
        input.hidden = false;
        input.disabled = false;
        input.required = true;
        input.value = state.alunoMatricula || input.value || "";
        return;
    }

    label.textContent = "Selecionar Aluno";
    label.setAttribute("for", "performanceStudentSelect");
    input.hidden = true;
    input.disabled = true;
    input.required = false;
    input.value = "";
    select.hidden = false;
    select.disabled = false;
    select.required = true;

    if (state.performanceAlunos.length === 0) {
        select.innerHTML = `<option value="" disabled selected>Nenhum aluno cadastrado</option>`;
        return;
    }

    const options = state.performanceAlunos.map((aluno) =>
        `<option value="${escapeHtml(aluno.id)}">Matrícula ${escapeHtml(aluno.id)} - ${escapeHtml(aluno.nome || `Aluno ${aluno.id}`)}</option>`
    ).join("");

    select.innerHTML = `<option value="">Escolha um aluno</option>${options}`;
}

async function loadPerformanceView() {
    if (state.perfil === "aluno") {
        state.performanceAlunos = [];
        document.getElementById("performanceSelectSubtitle").textContent = "Informe sua matrícula para visualizar o desempenho completo";
        document.getElementById("performanceSubmitButton").textContent = "Visualizar meu desempenho";
        showPerformanceSelect(message("Digite sua matrícula para consultar os dados calculados pelo backend."));
        renderPerformanceStudentOptions();
        return;
    }

    document.getElementById("performanceSelectSubtitle").textContent = "Selecione um aluno para visualizar o desempenho completo";
    document.getElementById("performanceStudentLabel").textContent = "Selecionar Aluno";
    document.getElementById("performanceSubmitButton").textContent = "Visualizar Desempenho";
    showPerformanceSelect(message("Carregando alunos..."));

    try {
        state.performanceAlunos = await requestJson("/alunos");
        renderPerformanceStudentOptions();
        document.getElementById("performanceFeedback").innerHTML = "";
    } catch (error) {
        state.performanceAlunos = [];
        renderPerformanceStudentOptions();
        document.getElementById("performanceFeedback").innerHTML = message("Não foi possível carregar os alunos cadastrados.", "error");
    }
}

function riskAlertClass(nivelRisco) {
    return `performance-alert ${riskClass(nivelRisco)}`;
}

function formatPerformanceStatus(status) {
    const normalized = normalize(status);
    if (normalized.includes("recuperacao") || normalized.includes("recuperação")) {
        return "Recuperação";
    }
    if (normalized.includes("reprovado")) {
        return "Reprovado";
    }
    if (normalized.includes("aprovado")) {
        return "Aprovado";
    }
    return status || "-";
}

function renderPerformanceExamHistory(historico) {
    const list = document.getElementById("performanceExamHistory");
    if (!historico || historico.length === 0) {
        list.innerHTML = message("Nenhum simulado encontrado para este aluno.");
        return;
    }

    list.innerHTML = historico.map((simulado) => `
        <div class="compact-row performance-history-row">
            <span>
                <span class="row-title">${escapeHtml(simulado.nomeSimulado || `Simulado ${simulado.simuladoId}`)}</span>
                <span class="row-subtitle">${escapeHtml(simulado.quantidadeNotas ?? 0)} notas</span>
            </span>
            <span class="muted">Média</span>
            <strong>${formatNumber(simulado.mediaPonderada)}</strong>
        </div>
    `).join("");
}


function renderPerformancePanel(analise, aluno) {
    document.getElementById("performanceSelectView").hidden = true;
    document.getElementById("performancePanelView").hidden = false;
    document.getElementById("performanceStudentName").textContent = aluno?.nome || `Aluno ${analise.alunoId}`;
    document.getElementById("changePerformanceStudentButton").hidden = state.perfil === "aluno";
    document.getElementById("performanceAverage").textContent = formatNumber(analise.mediaGeral);
    document.getElementById("performanceSituation").textContent = formatPerformanceStatus(analise.situacaoAcademica);
    document.getElementById("performanceRisk").textContent = analise.nivelRisco || "-";
    document.getElementById("performanceRankingPosition").textContent = analise.posicaoRanking
        ? `#${analise.posicaoRanking}`
        : "-";

    const riskAlert = document.getElementById("performanceRiskAlert");
    riskAlert.className = riskAlertClass(analise.nivelRisco);
    riskAlert.textContent = analise.alerta || "Desempenho estável e satisfatório.";

    const rankingAlert = document.getElementById("performanceRankingAlert");
    rankingAlert.className = `performance-ranking-alert ${analise.alunoNoTop10 ? "top" : ""}`;
    rankingAlert.textContent = analise.mensagemRanking || "Ranking acadêmico indisponível para este aluno.";

    renderPerformanceExamHistory(analise.historicoSimulados);
}

async function handlePerformanceSubmit(event) {
    event.preventDefault();
    const alunoId = state.perfil === "aluno"
        ? getStudentEnrollmentFromForm(event.currentTarget)
        : new FormData(event.currentTarget).get("alunoId");
    const feedback = document.getElementById("performanceFeedback");

    if (!alunoId) {
        feedback.innerHTML = message("Informe a matrícula do aluno.", "error");
        return;
    }
    if (state.perfil === "aluno" && !isPositiveEnrollment(alunoId)) {
        feedback.innerHTML = message("A matrícula deve ser um número inteiro positivo.", "error");
        return;
    }

    if (state.perfil === "aluno") {
        rememberStudentEnrollment(alunoId);
    }

    const aluno = state.perfil === "aluno"
        ? { id: alunoId, nome: `Matrícula ${alunoId}` }
        : state.performanceAlunos.find((item) => String(item.id) === String(alunoId));
    feedback.innerHTML = message("Carregando desempenho...");

    try {
        const analise = await requestJson(`/alunos/${encodeURIComponent(alunoId)}/desempenho`);
        renderPerformancePanel(analise, aluno);
    } catch (error) {
        feedback.innerHTML = message(error.message || "Não foi possível carregar o desempenho do aluno.", "error");
    }
}

function handleChangePerformanceStudent() {
    document.getElementById("performanceForm").reset();
    showPerformanceSelect();
    renderPerformanceStudentOptions();
}

function renderNotifications(notifications) {
    if (!notifications || notifications.length === 0) {
        return message("Nenhuma notificação encontrada para este responsável.");
    }

    return notifications.map((notificacao) => {
        const prioridade = notificacao.prioridade || "-";
        const lida = String(notificacao.status || "").toUpperCase() === "LIDA";
        const titulo = notificacao.mensagem || "Notificação acadêmica";
        return `
            <article class="notification-card">
                <div class="card-title-row">
                    <h3>${escapeHtml(titulo)}</h3>
                    <span class="status-pill ${statusClass(prioridade)}">${escapeHtml(prioridade)}</span>
                </div>
                <div class="notification-meta">
                    <span>Risco: ${escapeHtml(notificacao.nivelRisco || "-")}</span>
                    <span>Status: ${escapeHtml(notificacao.status || "-")}</span>
                    <span>Criada em: ${escapeHtml(notificacao.dataCriacao || "-")}</span>
                </div>
                ${lida ? "" : `
                    <div class="notification-actions">
                        <button
                            class="table-action success"
                            type="button"
                            data-notification-action="read"
                            data-notification-id="${escapeHtml(notificacao.id)}"
                            data-notification-responsavel-id="${escapeHtml(notificacao.responsavelId)}"
                        >
                            Marcar como lida
                        </button>
                    </div>
                `}
            </article>
        `;
    }).join("");
}

async function handleNotificationsSubmit(event) {
    event.preventDefault();
    const alunoId = new FormData(event.currentTarget).get("alunoId");
    const list = document.getElementById("notificationsList");
    list.innerHTML = message("Buscando vínculo do aluno...");

    try {
        const aluno = await requestJson(`/alunos/${encodeURIComponent(alunoId)}`);
        const responsavelId = aluno?.responsavelId;
        if (!responsavelId) {
            state.notificationsResponsavelId = null;
            list.innerHTML = message("Este aluno não possui responsável vinculado.", "error");
            return;
        }
        state.notificationsResponsavelId = responsavelId;
        list.innerHTML = message("Carregando notificações...");
        const notifications = await requestJson(`/responsaveis/${encodeURIComponent(responsavelId)}/notificacoes`);
        list.innerHTML = renderNotifications(notifications);
    } catch (error) {
        list.innerHTML = message(error.message, "error");
    }
}

async function markNotificationAsRead(responsavelId, notificationId) {
    const list = document.getElementById("notificationsList");
    list.innerHTML = message("Atualizando notificação...");

    try {
        await requestJson(`/responsaveis/${encodeURIComponent(responsavelId)}/notificacoes/${encodeURIComponent(notificationId)}/lida`, {
            method: "PATCH"
        });
        const notifications = await requestJson(`/responsaveis/${encodeURIComponent(responsavelId)}/notificacoes`);
        list.innerHTML = renderNotifications(notifications);
    } catch (error) {
        list.innerHTML = message(error.message || "Não foi possível marcar a notificação como lida.", "error");
    }
}

function handleNotificationsClick(event) {
    const button = event.target.closest("[data-notification-action]");
    if (!button) {
        return;
    }

    if (button.dataset.notificationAction === "read") {
        const responsavelId = button.dataset.notificationResponsavelId || state.notificationsResponsavelId;
        markNotificationAsRead(responsavelId, button.dataset.notificationId);
    }
}

function portalResultFromSettled(result) {
    if (result.status === "fulfilled") {
        return { ok: true, data: result.value };
    }

    return {
        ok: false,
        error: result.reason?.message || "A API não autorizou esta consulta."
    };
}

function portalBlockedCard(title, error) {
    return `
        <article class="card">
            <h3>${escapeHtml(title)}</h3>
            ${message(error, "error")}
        </article>
    `;
}

function portalRiskCard(desempenhoResult) {
    if (!desempenhoResult.ok) {
        return `
            <article class="card portal-risk-card blocked">
                <h3>Risco Acadêmico</h3>
                <p class="muted">Campo obrigatório do portal do responsável.</p>
                ${message("Risco bloqueado. Libere a permissão de desempenho para visualizar este dado.", "error")}
            </article>
        `;
    }

    const desempenho = desempenhoResult.data || {};
    const className = riskClass(desempenho.nivelRisco);

    return `
        <article class="card portal-risk-card ${className}">
            <h3>Risco Acadêmico</h3>
            <p class="muted">Visível somente quando a permissão de desempenho está liberada.</p>
            <div class="portal-risk-value">
                ${renderRiskPill(desempenho.nivelRisco)}
                <strong>${escapeHtml(desempenho.alerta || "Sem alerta registrado.")}</strong>
            </div>
        </article>
    `;
}

function renderPortalSuccess(desempenhoResult, notasResult, simuladosResult) {
    const desempenho = desempenhoResult.data || {};
    const notas = Array.isArray(notasResult.data) ? notasResult.data : [];
    const simulados = Array.isArray(simuladosResult.data) ? simuladosResult.data : [];

    return `
        ${desempenhoResult.ok ? `
            <article class="card">
                <h3>Desempenho</h3>
                <p class="muted">${escapeHtml(desempenho.alerta || "Consulta autorizada pelo backend.")}</p>
                <div class="portal-meta">
                    <span>Média: ${formatNumber(desempenho.mediaGeral)}</span>
                    <span>Total notas: ${escapeHtml(desempenho.totalNotas ?? "-")}</span>
                </div>
            </article>
        ` : portalBlockedCard("Desempenho", desempenhoResult.error)}
        ${portalRiskCard(desempenhoResult)}
        ${notasResult.ok ? `
            <article class="card">
                <h3>Notas</h3>
                <p class="muted">${escapeHtml(notas.length)} registros retornados pela API.</p>
                <div class="compact-list">
                    ${notas.slice(0, 4).map((nota) => `
                        <div class="compact-row">
                            <span>Disciplina ${escapeHtml(nota.disciplinaId ?? "-")}</span>
                            <strong>${formatNumber(nota.valor)}</strong>
                        </div>
                    `).join("") || `<span class="muted">Sem notas visíveis.</span>`}
                </div>
            </article>
        ` : portalBlockedCard("Notas", notasResult.error)}
        ${simuladosResult.ok ? `
            <article class="card">
                <h3>Simulados</h3>
                <p class="muted">${escapeHtml(simulados.length)} simulados disponíveis.</p>
                <div class="compact-list">
                    ${simulados.slice(0, 6).map((simulado) => `
                        <div class="compact-row">
                            <span>${escapeHtml(simulado?.descricao || "Simulado")}</span>
                            <strong>ID ${escapeHtml(simulado?.id ?? simulado ?? "-")}</strong>
                        </div>
                    `).join("") || `<span class="muted">Sem simulados visíveis.</span>`}
                </div>
            </article>
        ` : portalBlockedCard("Simulados", simuladosResult.error)}
    `;
}

async function handlePortalSubmit(event) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const alunoId = formData.get("alunoId");
    const result = document.getElementById("portalResult");
    result.innerHTML = message("Buscando vínculo do aluno...");

    try {
        const aluno = await requestJson(`/alunos/${encodeURIComponent(alunoId)}`);
        const responsavelId = aluno?.responsavelId;
        if (!responsavelId) {
            result.innerHTML = `<div class="card">${message("Este aluno não possui responsável vinculado.", "error")}</div>`;
            return;
        }

        result.innerHTML = message("Validando acesso no backend...");
        const base = `/responsaveis/${encodeURIComponent(responsavelId)}/alunos/${encodeURIComponent(alunoId)}`;
        const [desempenhoResponse, notasResponse, simuladosResponse] = await Promise.allSettled([
            requestJson(`${base}/desempenho`),
            requestJson(`${base}/notas`),
            requestJson(`${base}/simulados`)
        ]);

        const desempenho = portalResultFromSettled(desempenhoResponse);
        const notas = portalResultFromSettled(notasResponse);
        const simulados = portalResultFromSettled(simuladosResponse);

        result.innerHTML = renderPortalSuccess(desempenho, notas, simulados);
    } catch (error) {
        result.innerHTML = `<div class="card">${message(`Acesso bloqueado pela API: ${error.message}`, "error")}</div>`;
    }
}

function renderStudentStatus(status) {
    const className = normalize(status) === "ativo" ? "active" : "inactive";
    return `<span class="student-status ${className}">${escapeHtml(status)}</span>`;
}

function getClassName(turmaId) {
    const turma = state.turmas.find((item) => String(item.id) === String(turmaId));
    if (turma) {
        return turma.nome;
    }
    return turmaId ? `Turma ${turmaId}` : "Não vinculada";
}

function mapStudentFromApi(aluno) {
    return {
        id: aluno.id,
        nome: aluno.nome || "-",
        email: aluno.email || "-",
        turmaId: aluno.turmaId,
        turma: getClassName(aluno.turmaId),
        situacao: aluno.situacao || "-"
    };
}

function renderClassOptions() {
    const select = document.getElementById("studentClass");
    if (!select) {
        return;
    }

    if (state.turmas.length === 0) {
        select.innerHTML = `<option value="" disabled selected>Nenhuma turma cadastrada</option>`;
        return;
    }

    const options = state.turmas.map((turma) =>
        `<option value="${escapeHtml(turma.id)}">${escapeHtml(turma.nome)}</option>`
    ).join("");

    select.innerHTML = `<option value="">Selecione a turma</option>${options}`;
}

function uniqueClasses(turmas) {
    const seen = new Set();
    return turmas.filter((turma) => {
        const key = normalizeClassName(turma.nome);
        if (seen.has(key)) {
            return false;
        }
        seen.add(key);
        return true;
    });
}

function renderClassesTable() {
    const tbody = document.getElementById("classesTableBody");
    if (!tbody) {
        return;
    }

    if (state.turmas.length === 0) {
        tbody.innerHTML = `<tr><td class="muted">Nenhuma turma cadastrada.</td></tr>`;
        return;
    }

    tbody.innerHTML = state.turmas.map((turma) => `
        <tr>
            <td>${escapeHtml(turma.nome || "-")}</td>
        </tr>
    `).join("");
}

function renderStudentsTable() {
    const tbody = document.getElementById("studentsTableBody");
    if (state.alunos.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6" class="muted">Nenhum aluno cadastrado.</td></tr>`;
        return;
    }

    tbody.innerHTML = state.alunos.map((aluno) => {
        const inactive = normalize(aluno.situacao) === "inativo";
        const action = inactive
            ? `<button class="table-action success" type="button" data-student-action="active" data-student-id="${escapeHtml(aluno.id)}">Ativar</button>`
            : `<button class="table-action danger" type="button" data-student-action="inactive" data-student-id="${escapeHtml(aluno.id)}">Inativar</button>`;

        return `
            <tr>
                <td>${escapeHtml(aluno.id)}</td>
                <td>${escapeHtml(aluno.nome)}</td>
                <td>${escapeHtml(aluno.email)}</td>
                <td>${escapeHtml(aluno.turma)}</td>
                <td>${renderStudentStatus(aluno.situacao)}</td>
                <td>
                    <div class="table-actions">
                        <button class="table-action" type="button" data-student-action="edit" data-student-id="${escapeHtml(aluno.id)}">Editar</button>
                        ${action}
                    </div>
                </td>
            </tr>
        `;
    }).join("");
}

function showStudentsList(feedback = "") {
    document.getElementById("studentsListView").hidden = false;
    document.getElementById("studentFormView").hidden = true;
    document.getElementById("studentEditFormView").hidden = true;
    document.getElementById("studentFormFeedback").innerHTML = "";
    document.getElementById("studentsFeedback").innerHTML = feedback;
    renderStudentsTable();
}

async function loadStudents() {
    const feedback = document.getElementById("studentsFeedback");
    feedback.innerHTML = message("Carregando alunos...");

    try {
        const alunos = await requestJson("/alunos");
        state.alunos = alunos.map(mapStudentFromApi);
        feedback.innerHTML = "";
        renderStudentsTable();
    } catch (error) {
        state.alunos = [];
        renderStudentsTable();
        feedback.innerHTML = message("Não foi possível carregar os alunos. Tente novamente em alguns instantes.", "error");
    }
}

async function loadClasses(showFeedback = false, feedbackElementId = "studentFormFeedback") {
    const feedback = document.getElementById(feedbackElementId);
    if (showFeedback && feedback) {
        feedback.innerHTML = message("Carregando turmas...");
    }

    try {
        const turmas = await requestJson("/turmas");
        state.turmas = uniqueClasses(turmas);
        renderClassOptions();
        renderClassesTable();
        if (showFeedback && feedback) {
            feedback.innerHTML = "";
        }
    } catch (error) {
        state.turmas = [];
        renderClassOptions();
        renderClassesTable();
        if (showFeedback && feedback) {
            feedback.innerHTML = message("Não foi possível carregar as turmas. Tente novamente em alguns instantes.", "error");
        }
    }
}

async function loadStudentsView() {
    await loadClasses(false);
    await loadStudents();
}

function showClassesList(feedback = "") {
    document.getElementById("classesListView").hidden = false;
    document.getElementById("classFormView").hidden = true;
    document.getElementById("classFormFeedback").innerHTML = "";
    document.getElementById("classesFeedback").innerHTML = feedback;
    renderClassesTable();
}

async function loadClassesView() {
    await loadClasses(true, "classesFeedback");
}

async function showStudentForm() {
    document.getElementById("studentsListView").hidden = true;
    document.getElementById("studentFormView").hidden = false;
    document.getElementById("studentForm").reset();
    document.getElementById("studentFormFeedback").innerHTML = "";
    renderClassOptions();
    await loadClasses(true);
}

function showClassForm() {
    document.getElementById("classesListView").hidden = true;
    document.getElementById("classFormView").hidden = false;
    document.getElementById("classForm").reset();
    document.getElementById("classFormFeedback").innerHTML = "";
}

async function handleStudentSubmit(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const formData = new FormData(form);
    const nome = String(formData.get("nome") || "").trim();
    const email = String(formData.get("email") || "").trim();
    const turmaId = String(formData.get("turmaId") || "").trim();
    const feedback = document.getElementById("studentFormFeedback");

    feedback.innerHTML = message("Salvando aluno...");

    try {
        const aluno = await requestJson("/alunos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ nome, email })
        });

        if (turmaId) {
            await requestJson(`/alunos/${encodeURIComponent(aluno.id)}/turma`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ turmaId: Number(turmaId) })
            });
        }

        showStudentsList();
        await loadStudentsView();
        document.getElementById("studentsFeedback").innerHTML = message("Aluno cadastrado com sucesso.", "ok");
    } catch (error) {
        feedback.innerHTML = message(error.message, "error");
    }
}

async function updateStudentStatus(alunoId, shouldActivate) {
    const feedback = document.getElementById("studentsFeedback");
    feedback.innerHTML = message(shouldActivate ? "Ativando aluno..." : "Inativando aluno...");

    try {
        await requestJson(`/alunos/${encodeURIComponent(alunoId)}/${shouldActivate ? "ativar" : "inativar"}`, {
            method: "PATCH"
        });
        await loadStudentsView();
        document.getElementById("studentsFeedback").innerHTML = message(shouldActivate ? "Aluno ativado com sucesso." : "Aluno inativado com sucesso.", "ok");
    } catch (error) {
        feedback.innerHTML = message(error.message || "Não foi possível atualizar o aluno.", "error");
    }
}

function renderEditClassOptions(selectedTurmaId) {
    const select = document.getElementById("studentEditClass");
    if (!select) return;
    const options = state.turmas.map((turma) =>
        `<option value="${escapeHtml(turma.id)}" ${String(turma.id) === String(selectedTurmaId) ? "selected" : ""}>${escapeHtml(turma.nome)}</option>`
    ).join("");
    select.innerHTML = `<option value="">Selecione a turma</option>${options}`;
}

function showStudentEditForm(alunoId) {
    const aluno = state.alunos.find((a) => String(a.id) === String(alunoId));
    if (!aluno) {
        return;
    }
    document.getElementById("studentsListView").hidden = true;
    document.getElementById("studentFormView").hidden = true;
    document.getElementById("studentEditFormView").hidden = false;
    document.getElementById("studentEditName").value = aluno.nome;
    document.getElementById("studentEditEmail").value = aluno.email;
    renderEditClassOptions(aluno.turmaId);
    document.getElementById("studentEditFormFeedback").innerHTML = "";
    document.getElementById("studentEditForm").dataset.alunoId = alunoId;
}

async function handleStudentEditSubmit(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const alunoId = form.dataset.alunoId;
    const formData = new FormData(form);
    const nome = String(formData.get("nome") || "").trim();
    const email = String(formData.get("email") || "").trim();
    const turmaIdRaw = formData.get("turmaId");
    const turmaId = turmaIdRaw ? Number(turmaIdRaw) : null;
    const alunoAtual = state.alunos.find((aluno) => String(aluno.id) === String(alunoId));
    const feedback = document.getElementById("studentEditFormFeedback");

    feedback.innerHTML = message("Salvando alterações...");

    try {
        await requestJson(`/alunos/${encodeURIComponent(alunoId)}`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nome, email })
        });

        if (turmaId && (!alunoAtual || String(alunoAtual.turmaId) !== String(turmaId))) {
            await requestJson(`/alunos/${encodeURIComponent(alunoId)}/turma`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ turmaId })
            });
        }

        await loadStudentsView();
        showStudentsList(message("Aluno atualizado com sucesso.", "ok"));
    } catch (error) {
        feedback.innerHTML = message(error.message, "error");
    }
}

function handleStudentsTableClick(event) {
    const button = event.target.closest("[data-student-action]");
    if (!button) {
        return;
    }

    const action = button.dataset.studentAction;
    if (action === "edit") {
        showStudentEditForm(button.dataset.studentId);
    } else {
        updateStudentStatus(button.dataset.studentId, action === "active");
    }
}

async function handleClassSubmit(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const nome = String(new FormData(form).get("nome") || "").trim();
    const feedback = document.getElementById("classFormFeedback");

    if (!nome) {
        feedback.innerHTML = message("Informe o nome da turma.", "error");
        return;
    }

    if (state.turmas.some((turma) => normalizeClassName(turma.nome) === normalizeClassName(nome))) {
        feedback.innerHTML = message("Já existe uma turma cadastrada com esse nome.", "error");
        return;
    }

    feedback.innerHTML = message("Salvando turma...");

    try {
        await requestJson("/turmas", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nome })
        });

        await loadClasses(false);
        showClassesList(message("Turma cadastrada com sucesso.", "ok"));
    } catch (error) {
        feedback.innerHTML = message(error.message, "error");
    }
}

function normalizeSubjectStatus(status) {
    return normalize(status) === "inativa" ? "Inativa" : "Ativa";
}

function renderSubjectStatus(status) {
    const label = normalizeSubjectStatus(status);
    const className = label === "Ativa" ? "active" : "inactive";
    return `<span class="student-status ${className}">${escapeHtml(label)}</span>`;
}

function mapSubjectFromApi(disciplina) {
    return {
        id: disciplina.id,
        nome: disciplina.nome || "-",
        status: normalizeSubjectStatus(disciplina.status)
    };
}

function renderSubjectsTable() {
    const tbody = document.getElementById("subjectsTableBody");
    if (state.disciplinas.length === 0) {
        tbody.innerHTML = `<tr><td colspan="3" class="muted">Nenhuma disciplina cadastrada.</td></tr>`;
        return;
    }

    tbody.innerHTML = state.disciplinas.map((disciplina) => {
        const action = disciplina.status === "Ativa"
            ? `<button class="table-action danger" type="button" data-subject-action="inactive" data-subject-id="${escapeHtml(disciplina.id)}">Inativar</button>`
            : `<button class="table-action success" type="button" data-subject-action="active" data-subject-id="${escapeHtml(disciplina.id)}">Ativar</button>`;

        return `
            <tr>
                <td>${escapeHtml(disciplina.nome)}</td>
                <td>${renderSubjectStatus(disciplina.status)}</td>
                <td>
                    <div class="table-actions">
                        <button class="table-action" type="button" data-subject-action="details" data-subject-id="${escapeHtml(disciplina.id)}">Ver Detalhes</button>
                        <button class="table-action" type="button" data-subject-action="edit" data-subject-id="${escapeHtml(disciplina.id)}">Editar</button>
                        ${action}
                    </div>
                </td>
            </tr>
        `;
    }).join("");
}

function showSubjectsList(feedback = "") {
    document.getElementById("subjectsListView").hidden = false;
    document.getElementById("subjectFormView").hidden = true;
    document.getElementById("subjectEditFormView").hidden = true;
    document.getElementById("subjectFormFeedback").innerHTML = "";
    document.getElementById("subjectsFeedback").innerHTML = feedback;
    renderSubjectsTable();
}

async function loadSubjects() {
    const feedback = document.getElementById("subjectsFeedback");
    feedback.innerHTML = message("Carregando disciplinas...");

    try {
        const disciplinas = await requestJson("/disciplinas");
        state.disciplinas = disciplinas.map(mapSubjectFromApi);
        feedback.innerHTML = "";
        renderSubjectsTable();
    } catch (error) {
        state.disciplinas = [];
        renderSubjectsTable();
        feedback.innerHTML = message("Não foi possível carregar as disciplinas. Tente novamente em alguns instantes.", "error");
    }
}

function showSubjectForm() {
    document.getElementById("subjectsListView").hidden = true;
    document.getElementById("subjectFormView").hidden = false;
    document.getElementById("subjectForm").reset();
    document.getElementById("subjectFormFeedback").innerHTML = "";
    closeSubjectDetails();
}

function closeSubjectDetails() {
    document.getElementById("subjectDetailsCard").hidden = true;
    document.getElementById("subjectDetailsContent").innerHTML = "";
}

async function showSubjectDetails(disciplinaId) {
    const detailsCard = document.getElementById("subjectDetailsCard");
    const content = document.getElementById("subjectDetailsContent");
    detailsCard.hidden = false;
    content.innerHTML = message("Carregando detalhes...");

    try {
        const disciplina = mapSubjectFromApi(await requestJson(`/disciplinas/${encodeURIComponent(disciplinaId)}`));
        content.innerHTML = `
            <div class="subject-details-grid">
                <span>
                    <strong>Nome da disciplina</strong>
                    <small>${escapeHtml(disciplina.nome)}</small>
                </span>
                <span>
                    <strong>Status atual</strong>
                    ${renderSubjectStatus(disciplina.status)}
                </span>
            </div>
        `;
    } catch (error) {
        content.innerHTML = message(error.message, "error");
    }
}

async function updateSubjectStatus(disciplinaId, shouldActivate) {
    const feedback = document.getElementById("subjectsFeedback");
    feedback.innerHTML = message(shouldActivate ? "Ativando disciplina..." : "Inativando disciplina...");

    try {
        await requestJson(
            shouldActivate
                ? `/disciplinas/${encodeURIComponent(disciplinaId)}/ativar`
                : `/disciplinas/${encodeURIComponent(disciplinaId)}/inativar`,
            { method: "PATCH" }
        );
        closeSubjectDetails();
        await loadSubjects();
    } catch (error) {
        feedback.innerHTML = message(error.message, "error");
    }
}

async function deleteSubject(disciplinaId) {
    const feedback = document.getElementById("subjectsFeedback");
    const confirmed = window.confirm("Deseja excluir esta disciplina definitivamente?");
    if (!confirmed) {
        return;
    }

    feedback.innerHTML = message("Excluindo disciplina...");

    try {
        await requestJson(`/disciplinas/${encodeURIComponent(disciplinaId)}`, {
            method: "DELETE"
        });
        closeSubjectDetails();
        await loadSubjects();
        document.getElementById("subjectsFeedback").innerHTML = message("Disciplina excluída com sucesso.", "ok");
    } catch (error) {
        feedback.innerHTML = message(error.message, "error");
    }
}

async function handleSubjectSubmit(event) {
    event.preventDefault();
    const nome = String(new FormData(event.currentTarget).get("nome") || "").trim();
    const feedback = document.getElementById("subjectFormFeedback");
    feedback.innerHTML = message("Salvando disciplina...");

    try {
        await requestJson("/disciplinas", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ nome })
        });

        showSubjectsList();
        await loadSubjects();
        document.getElementById("subjectsFeedback").innerHTML = message("Disciplina cadastrada com sucesso.", "ok");
    } catch (error) {
        feedback.innerHTML = message(error.message, "error");
    }
}

function showSubjectEditForm(disciplinaId) {
    const disciplina = state.disciplinas.find((d) => String(d.id) === String(disciplinaId));
    if (!disciplina) {
        return;
    }
    document.getElementById("subjectsListView").hidden = true;
    document.getElementById("subjectFormView").hidden = true;
    document.getElementById("subjectEditFormView").hidden = false;
    document.getElementById("subjectEditName").value = disciplina.nome;
    document.getElementById("subjectEditFormFeedback").innerHTML = "";
    document.getElementById("subjectEditForm").dataset.disciplinaId = disciplinaId;
}

async function handleSubjectEditSubmit(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const disciplinaId = form.dataset.disciplinaId;
    const nome = String(new FormData(form).get("nome") || "").trim();
    const feedback = document.getElementById("subjectEditFormFeedback");

    feedback.innerHTML = message("Salvando alterações...");

    try {
        await requestJson(`/disciplinas/${encodeURIComponent(disciplinaId)}`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nome })
        });
        await loadSubjects();
        showSubjectsList(message("Disciplina atualizada com sucesso.", "ok"));
    } catch (error) {
        feedback.innerHTML = message(error.message, "error");
    }
}

function handleSubjectTableClick(event) {
    const button = event.target.closest("[data-subject-action]");
    if (!button) {
        return;
    }

    const disciplinaId = button.dataset.subjectId;
    const action = button.dataset.subjectAction;
    if (action === "details") {
        showSubjectDetails(disciplinaId);
    } else if (action === "edit") {
        showSubjectEditForm(disciplinaId);
    } else if (action === "delete") {
        deleteSubject(disciplinaId);
    } else {
        updateSubjectStatus(disciplinaId, action === "active");
    }
}

function renderNoteSelect(selectId, items, placeholder, emptyMessage, labelBuilder) {
    const select = document.getElementById(selectId);
    if (items.length === 0) {
        select.innerHTML = `<option value="" disabled selected>${escapeHtml(emptyMessage)}</option>`;
        return;
    }

    const options = items.map((item) =>
        `<option value="${escapeHtml(item.id)}">${escapeHtml(labelBuilder(item))}</option>`
    ).join("");

    select.innerHTML = `<option value="">${escapeHtml(placeholder)}</option>${options}`;
}

function renderNoteFormOptions() {
    renderNoteSelect(
        "noteStudentSelect",
        state.notaAlunos,
        "Escolha um aluno",
        "Nenhum aluno cadastrado",
        (aluno) => `Matrícula ${aluno.id} - ${aluno.nome || `Aluno ${aluno.id}`}`
    );
    renderNoteSelect(
        "noteExamSelect",
        state.notaSimulados,
        "Escolha um simulado",
        "Nenhum simulado cadastrado",
        (simulado) => simulado.descricao || `Simulado ${simulado.id}`
    );
    renderNoteSelect(
        "noteSubjectSelect",
        state.notaDisciplinas,
        "Escolha uma disciplina",
        state.notaSimuladoSelecionadoId ? "Nenhuma disciplina vinculada ao simulado" : "Selecione um simulado primeiro",
        (disciplina) => disciplina.nome || `Disciplina ${disciplina.id}`
    );
}

async function handleNoteExamChange(event) {
    const simuladoId = String(event.target.value || "");
    state.notaSimuladoSelecionadoId = simuladoId;

    const selectDisciplina = document.getElementById("noteSubjectSelect");

    state.notaDisciplinas = [];
    selectDisciplina.innerHTML = `<option value="">Carregando disciplinas...</option>`;

    if (!simuladoId) {
        selectDisciplina.innerHTML = `<option value="">Selecione um simulado primeiro</option>`;
        return;
    }

    try {
        const disciplinas = await requestJson(`/simulados/${encodeURIComponent(simuladoId)}/disciplinas`);

        state.notaDisciplinas = Array.isArray(disciplinas)
            ? disciplinas.filter((disciplina) => {
                const status = String(disciplina.status || "").toLowerCase();
                return status.includes("ativa") && !status.includes("inativa");
            })
            : [];

        if (state.notaDisciplinas.length === 0) {
            selectDisciplina.innerHTML = `<option value="">Nenhuma disciplina vinculada a este simulado</option>`;
            return;
        }

        selectDisciplina.innerHTML =
            `<option value="">Escolha uma disciplina</option>` +
            state.notaDisciplinas.map((disciplina) => `
                <option value="${escapeHtml(disciplina.id)}">
                    ${escapeHtml(disciplina.nome)}
                </option>
            `).join("");

        document.getElementById("noteFormFeedback").innerHTML = "";
    } catch (error) {
        state.notaDisciplinas = [];
        selectDisciplina.innerHTML = `<option value="">Erro ao carregar disciplinas</option>`;
        document.getElementById("noteFormFeedback").innerHTML =
            message("Não foi possível carregar as disciplinas desse simulado.", "error");
    }
}

function renderStudentNotesOptions() {
    if (state.perfil === "aluno") {
        const input = document.getElementById("studentNotesEnrollment");
        input.value = state.alunoMatricula || input.value || "";
        return;
    }
}

function renderStudentNotesList(notas) {
    const list = document.getElementById("studentNotesList");
    if (!notas || notas.length === 0) {
        list.innerHTML = message("Nenhuma nota encontrada para este aluno.");
        return;
    }

    list.innerHTML = notas.map((nota, index) => `
        <div class="compact-row">
            <div>
                <span class="row-title">Nota ${escapeHtml(index + 1)}</span>
                <span class="row-subtitle">${escapeHtml(nota.nomeDisciplina || `Disciplina ${nota.disciplinaId}`)} | ${escapeHtml(nota.descricaoSimulado || `Simulado ${nota.simuladoId}`)}</span>
            </div>
            <strong>${formatNumber(nota.valor)}</strong>
        </div>
    `).join("");
}

async function loadNotesView() {
    const feedback = document.getElementById("noteFormFeedback");
    const studentFeedback = document.getElementById("studentNotesFeedback");
    const studentList = document.getElementById("studentNotesList");

    if (state.perfil === "aluno") {
        document.getElementById("noteLaunchView").hidden = true;
        document.getElementById("studentNoteView").hidden = false;
        renderStudentNotesOptions();
        studentFeedback.innerHTML = message("Informe sua matrícula para consultar suas notas.");
        studentList.innerHTML = "";
        return;
    }

    document.getElementById("noteLaunchView").hidden = false;
    document.getElementById("studentNoteView").hidden = true;
    feedback.innerHTML = message("Carregando dados...");

    try {
        const [alunos, simulados] = await Promise.all([
            requestJson("/alunos"),
            requestJson("/simulados")
        ]);

        state.notaAlunos = alunos;
        state.notaSimulados = simulados;
        state.notaSimuladoSelecionadoId = null;
        state.notaDisciplinas = [];
        renderNoteFormOptions();
        feedback.innerHTML = message("Selecione um simulado para ver as disciplinas vinculadas.");
    } catch (error) {
        state.notaAlunos = [];
        state.notaSimulados = [];
        state.notaSimuladoSelecionadoId = null;
        state.notaDisciplinas = [];
        renderNoteFormOptions();
        feedback.innerHTML = message("Não foi possível carregar os dados para lançamento de nota.", "error");
    }
}

function clearNoteForm(clearFeedback = true) {
    document.getElementById("noteForm").reset();
    state.notaSimuladoSelecionadoId = null;
    state.notaDisciplinas = [];
    renderNoteSelect(
        "noteSubjectSelect",
        [],
        "Escolha uma disciplina",
        "Selecione um simulado primeiro",
        (disciplina) => disciplina.nome || `Disciplina ${disciplina.id}`
    );
    if (clearFeedback) {
        document.getElementById("noteFormFeedback").innerHTML = "";
    }
}

function validateNoteFormData(formData) {
    const alunoId = formData.get("alunoId");
    const simuladoId = formData.get("simuladoId");
    const disciplinaId = formData.get("disciplinaId");
    const valorText = String(formData.get("valor") ?? "").trim();
    const valor = Number(valorText);

    if (!alunoId) {
        return "Selecione um aluno.";
    }
    if (!simuladoId) {
        return "Selecione um simulado.";
    }
    if (!disciplinaId) {
        return "Selecione uma disciplina.";
    }
    if (!state.notaDisciplinas.some((disciplina) => String(disciplina.id) === String(disciplinaId))) {
        return "A disciplina selecionada nao esta vinculada ao simulado.";
    }
    if (!valorText || Number.isNaN(valor)) {
        return "Informe uma nota de 0 a 10.";
    }
    if (valor < 0 || valor > 10) {
        return "A nota deve estar entre 0 e 10.";
    }

    return "";
}

async function handleNoteSubmit(event) {
    event.preventDefault();
    if (state.perfil !== "professor") {
        document.getElementById("noteFormFeedback").innerHTML = message("Somente professor pode lançar notas.", "error");
        return;
    }

    const formData = new FormData(event.currentTarget);
    const feedback = document.getElementById("noteFormFeedback");
    const validationMessage = validateNoteFormData(formData);

    if (validationMessage) {
        feedback.innerHTML = message(validationMessage, "error");
        return;
    }

    feedback.innerHTML = message("Lançando nota...");

    try {
        await requestJson("/notas", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                alunoId: Number(formData.get("alunoId")),
                simuladoId: Number(formData.get("simuladoId")),
                disciplinaId: Number(formData.get("disciplinaId")),
                valor: Number(formData.get("valor"))
            })
        });

        clearNoteForm(false);
        feedback.innerHTML = message("Nota lançada com sucesso.", "ok");
    } catch (error) {
        feedback.innerHTML = message(error.message || "Não foi possível lançar a nota.", "error");
    }
}

async function handleStudentNotesSubmit(event) {
    event.preventDefault();
    const alunoId = state.perfil === "aluno"
        ? getStudentEnrollmentFromForm(event.currentTarget)
        : new FormData(event.currentTarget).get("alunoId");
    const feedback = document.getElementById("studentNotesFeedback");
    const list = document.getElementById("studentNotesList");

    if (!alunoId) {
        feedback.innerHTML = message("Informe a matrícula do aluno.", "error");
        return;
    }
    if (state.perfil === "aluno" && !isPositiveEnrollment(alunoId)) {
        feedback.innerHTML = message("A matrícula deve ser um número inteiro positivo.", "error");
        return;
    }

    if (state.perfil === "aluno") {
        rememberStudentEnrollment(alunoId);
    }

    feedback.innerHTML = message("Carregando notas...");
    list.innerHTML = "";

    try {
        if (!state.disciplinaNomePorId || Object.keys(state.disciplinaNomePorId).length === 0) {
            const disciplinas = await requestJson("/disciplinas");
            state.disciplinaNomePorId = Array.isArray(disciplinas)
                ? disciplinas.reduce((acc, disciplina) => {
                    acc[String(disciplina.id)] = disciplina.nome || `ID ${disciplina.id}`;
                    return acc;
                }, {})
                : {};
        }
        if (!state.simuladoDescricaoPorId || Object.keys(state.simuladoDescricaoPorId).length === 0) {
            const simulados = await requestJson("/simulados");
            state.simuladoDescricaoPorId = Array.isArray(simulados)
                ? simulados.reduce((acc, simulado) => {
                    acc[String(simulado.id)] = simulado.descricao || `ID ${simulado.id}`;
                    return acc;
                }, {})
                : {};
        }
        const notas = await requestJson(`/notas/aluno/${encodeURIComponent(alunoId)}`);
        renderStudentNotesList(notas);
        feedback.innerHTML = "";
    } catch (error) {
        list.innerHTML = "";
        feedback.innerHTML = message(error.message || "Não foi possível carregar as notas.", "error");
    }
}

function isSubjectActive(subject) {
    const status = normalize(subject?.status);
    return status.includes("ativa") && !status.includes("inativa");
}

function renderSimulationConsistencyBadge(simulado) {
    const consistente = Boolean(simulado?.consistente);
    const label = simulado?.status || (consistente ? "Consistente" : "Inconsistente");
    return `<span class="status-pill ${consistente ? "success" : "danger"}">${escapeHtml(label)}</span>`;
}

function renderSimulationSubjectBadge(status) {
    const ativa = isSubjectActive({ status });
    return `<span class="student-status ${ativa ? "active" : "inactive"}">${ativa ? "Ativa" : "Inativa"}</span>`;
}

function showSimulationsList(feedback = "") {
    document.getElementById("simulationsListView").hidden = false;
    document.getElementById("simulationDetailsView").hidden = true;
    document.getElementById("simulationCreateView").hidden = true;
    document.getElementById("simuladoEditFormView").hidden = true;
    document.getElementById("simulationsFeedback").innerHTML = feedback;
    renderSimulationsTable();
}

function renderSimulationsTable() {
    const tbody = document.getElementById("simulationsTableBody");
    if (state.simulados.length === 0) {
        tbody.innerHTML = `<tr><td colspan="4" class="muted">Nenhum simulado cadastrado.</td></tr>`;
        return;
    }

    tbody.innerHTML = state.simulados.map((simulado) => `
        <tr>
            <td>
                <span class="row-title">${escapeHtml(simulado.descricao || `Simulado ${simulado.id}`)}</span>
                <span class="row-subtitle">ID ${escapeHtml(simulado.id)}</span>
            </td>
            <td>${escapeHtml(simulado.quantidadeDisciplinas ?? 0)}</td>
            <td>${renderSimulationConsistencyBadge(simulado)}</td>
            <td>
                <button class="table-action" type="button" data-simulation-action="details" data-simulation-id="${escapeHtml(simulado.id)}">Ver Detalhes</button>
                <button class="table-action" type="button" data-simulation-action="edit" data-simulation-id="${escapeHtml(simulado.id)}">Editar</button>
            </td>
        </tr>
    `).join("");
}

async function loadSimulationsView() {
    const feedback = document.getElementById("simulationsFeedback");
    feedback.innerHTML = message("Carregando simulados...");

    try {
        state.simulados = await requestJson("/simulados");
        renderSimulationsTable();
        feedback.innerHTML = "";
    } catch (error) {
        state.simulados = [];
        renderSimulationsTable();
        feedback.innerHTML = message(error.message || "Não foi possível carregar os simulados.", "error");
    }
}

function clearSimulationDetails() {
    document.getElementById("simulationDetailsTitle").textContent = "Detalhes do Simulado";
    document.getElementById("simulationDetailsSubtitle").textContent = "Composição e participação";
    document.getElementById("simulationDescription").textContent = "-";
    document.getElementById("simulationConsistencyBadge").outerHTML = `<span id="simulationConsistencyBadge" class="status-pill neutral">-</span>`;
    document.getElementById("simulationConsistencyAlert").innerHTML = "";
    document.getElementById("simulationLinkedSubjects").innerHTML = "";
    document.getElementById("simulationTotalNotes").textContent = "0";
    document.getElementById("simulationStudentsWithNotes").textContent = "0";
    document.getElementById("simulationParticipants").innerHTML = "";
}

async function showSimulationDetails(simuladoId) {
    document.getElementById("simulationsListView").hidden = true;
    document.getElementById("simulationCreateView").hidden = true;
    document.getElementById("simulationDetailsView").hidden = false;
    document.getElementById("simulationDetailsFeedback").innerHTML = message("Carregando detalhes do simulado...");
    clearSimulationDetails();

    try {
        const simulado = await requestJson(`/simulados/${encodeURIComponent(simuladoId)}`);
        renderSimulationDetails(simulado);
        document.getElementById("simulationDetailsFeedback").innerHTML = "";
    } catch (error) {
        document.getElementById("simulationDetailsFeedback").innerHTML = message(error.message || "Não foi possível carregar os detalhes.", "error");
    }
}

function renderSimulationDetails(simulado) {
    document.getElementById("simulationDetailsTitle").textContent = "Detalhes do Simulado";
    document.getElementById("simulationDetailsSubtitle").textContent = simulado.descricao || `Simulado ${simulado.id}`;
    document.getElementById("simulationDescription").textContent = simulado.descricao || `Simulado ${simulado.id}`;
    document.getElementById("simulationConsistencyBadge").outerHTML = renderSimulationConsistencyBadge(simulado).replace("<span", `<span id="simulationConsistencyBadge"`);

    const consistencyAlert = document.getElementById("simulationConsistencyAlert");
    consistencyAlert.innerHTML = simulado.consistente
        ? ""
        : `<div class="simulation-alert danger">${escapeHtml(simulado.motivoInconsistencia || "Este simulado possui problemas de consistência.")}</div>`;

    renderSimulationLinkedSubjects(simulado.disciplinas || []);
    renderSimulationNoteMetrics(simulado.notasRelacionadas || {});
    renderSimulationParticipants(simulado.alunosParticipantes || []);
}

function renderSimulationLinkedSubjects(subjects) {
    const list = document.getElementById("simulationLinkedSubjects");
    if (subjects.length === 0) {
        list.innerHTML = message("Nenhuma disciplina vinculada.");
        return;
    }

    list.innerHTML = subjects.map((subject) => `
        <div class="simulation-subject-row">
            <span>${escapeHtml(subject.nome || `Disciplina ${subject.id}`)}</span>
            ${renderSimulationSubjectBadge(subject.status)}
        </div>
    `).join("");
}

function renderSimulationNoteMetrics(notas) {
    document.getElementById("simulationTotalNotes").textContent = notas.totalNotas ?? 0;
    document.getElementById("simulationStudentsWithNotes").textContent = notas.alunosComNotas ?? 0;
}

function renderSimulationParticipants(participants) {
    const list = document.getElementById("simulationParticipants");
    if (participants.length === 0) {
        list.innerHTML = message("Nenhum aluno participante.");
        return;
    }

    list.innerHTML = participants.map((participant) => `
        <div class="compact-row simulation-participant-row">
            <span>
                <span class="row-title">${escapeHtml(participant.nome || `Aluno ${participant.alunoId}`)}</span>
                <span class="row-subtitle">${escapeHtml(participant.quantidadeNotas ?? 0)} nota(s)</span>
            </span>
            <span class="muted">Média</span>
            <strong>${formatNumber(participant.media)}</strong>
        </div>
    `).join("");
}

async function showSimulationCreateView() {
    document.getElementById("simulationsListView").hidden = true;
    document.getElementById("simulationDetailsView").hidden = true;
    document.getElementById("simulationCreateView").hidden = false;
    document.getElementById("simulationForm").reset();
    document.getElementById("simulationFormFeedback").innerHTML = message("Carregando disciplinas...");
    document.getElementById("simulationSubjectOptions").innerHTML = "";
    updateSimulationSelectedCounter();

    try {
        state.simuladoDisciplinas = await requestJson("/disciplinas");
        renderSimulationSubjectOptions();
        document.getElementById("simulationFormFeedback").innerHTML = "";
    } catch (error) {
        state.simuladoDisciplinas = [];
        renderSimulationSubjectOptions();
        document.getElementById("simulationFormFeedback").innerHTML = message(error.message || "Não foi possível carregar as disciplinas.", "error");
    }
}

function renderSimulationSubjectOptions() {
    const container = document.getElementById("simulationSubjectOptions");
    if (state.simuladoDisciplinas.length === 0) {
        container.innerHTML = message("Nenhuma disciplina cadastrada.");
        updateSimulationSelectedCounter();
        return;
    }

    container.innerHTML = state.simuladoDisciplinas.map((disciplina) => {
        const ativa = isSubjectActive(disciplina);
        return `
            <label class="simulation-subject-option ${ativa ? "" : "disabled"}">
                <input name="disciplinasIds" type="checkbox" value="${escapeHtml(disciplina.id)}" ${ativa ? "" : "disabled"}>
                <span>${escapeHtml(disciplina.nome || `Disciplina ${disciplina.id}`)}</span>
                ${renderSimulationSubjectBadge(disciplina.status)}
            </label>
        `;
    }).join("");
    updateSimulationSelectedCounter();
}

function getSelectedSimulationSubjectIds() {
    return Array.from(document.querySelectorAll("#simulationSubjectOptions input[name='disciplinasIds']:checked"))
        .map((input) => Number(input.value));
}

function updateSimulationSelectedCounter() {
    const counter = document.getElementById("simulationSelectedCounter");
    const total = getSelectedSimulationSubjectIds().length;
    counter.textContent = `${total} disciplina(s) selecionada(s)`;
}

function validateSimulationFormData(formData) {
    const descricao = String(formData.get("descricao") || "").trim();
    const disciplinasIds = getSelectedSimulationSubjectIds();

    if (!descricao) {
        return "Informe a descrição do simulado.";
    }

    if (disciplinasIds.length < 2) {
        return "Selecione pelo menos 2 disciplinas.";
    }

    const possuiInativa = disciplinasIds.some((id) => {
        const disciplina = state.simuladoDisciplinas.find((item) => Number(item.id) === id);
        return !isSubjectActive(disciplina);
    });

    if (possuiInativa) {
        return "Disciplinas inativas não podem ser selecionadas.";
    }

    return "";
}

async function handleSimulationSubmit(event) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const feedback = document.getElementById("simulationFormFeedback");
    const validationMessage = validateSimulationFormData(formData);

    if (validationMessage) {
        feedback.innerHTML = message(validationMessage, "error");
        return;
    }

    feedback.innerHTML = message("Criando simulado...");

    try {
        await requestJson("/simulados", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                descricao: String(formData.get("descricao") || "").trim(),
                disciplinasIds: getSelectedSimulationSubjectIds()
            })
        });

        showSimulationsList(message("Atualizando simulados..."));
        await loadSimulationsView();
        document.getElementById("simulationsFeedback").innerHTML = message("Simulado criado com sucesso.", "ok");
    } catch (error) {
        feedback.innerHTML = message(error.message || "Não foi possível criar o simulado.", "error");
    }
}

function getSelectedSimuladoEditSubjectIds() {
    return Array.from(document.querySelectorAll("#simuladoEditSubjectOptions input[name='disciplinasIds']:checked"))
        .map((input) => Number(input.value));
}

function updateSimuladoEditCounter() {
    const counter = document.getElementById("simuladoEditSelectedCounter");
    if (counter) {
        counter.textContent = `${getSelectedSimuladoEditSubjectIds().length} disciplina(s) selecionada(s)`;
    }
}

function renderSimuladoEditSubjectOptions(selectedIds) {
    const container = document.getElementById("simuladoEditSubjectOptions");
    if (!container) return;
    const selectedSet = new Set((selectedIds || []).map(String));
    if (state.simuladoDisciplinas.length === 0) {
        container.innerHTML = message("Nenhuma disciplina cadastrada.");
        updateSimuladoEditCounter();
        return;
    }
    container.innerHTML = state.simuladoDisciplinas.map((disciplina) => {
        const ativa = isSubjectActive(disciplina);
        const checked = selectedSet.has(String(disciplina.id)) && ativa;
        return `
            <label class="simulation-subject-option ${ativa ? "" : "disabled"}">
                <input name="disciplinasIds" type="checkbox" value="${escapeHtml(disciplina.id)}" ${checked ? "checked" : ""} ${ativa ? "" : "disabled"}>
                <span>${escapeHtml(disciplina.nome || `Disciplina ${disciplina.id}`)}</span>
                ${renderSimulationSubjectBadge(disciplina.status)}
            </label>
        `;
    }).join("");
    updateSimuladoEditCounter();
}

async function showSimulationEditForm(simuladoId) {
    document.getElementById("simulationsListView").hidden = true;
    document.getElementById("simulationDetailsView").hidden = true;
    document.getElementById("simulationCreateView").hidden = true;
    document.getElementById("simuladoEditFormView").hidden = false;
    document.getElementById("simuladoEditFormFeedback").innerHTML = message("Carregando...");
    document.getElementById("simuladoEditSubjectOptions").innerHTML = "";
    updateSimuladoEditCounter();

    try {
        const [detalhe, disciplinas] = await Promise.all([
            requestJson(`/simulados/${encodeURIComponent(simuladoId)}`),
            requestJson("/disciplinas")
        ]);
        state.simuladoDisciplinas = disciplinas;
        const selectedIds = (detalhe.disciplinas || []).map((d) => d.disciplinaId ?? d.id);
        document.getElementById("simuladoEditDescricao").value = detalhe.descricao || "";
        document.getElementById("simuladoEditForm").dataset.simuladoId = simuladoId;
        renderSimuladoEditSubjectOptions(selectedIds);
        document.getElementById("simuladoEditFormFeedback").innerHTML = "";
    } catch (error) {
        document.getElementById("simuladoEditFormFeedback").innerHTML = message(error.message || "Erro ao carregar simulado.", "error");
    }
}

async function handleSimuladoEditSubmit(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const simuladoId = form.dataset.simuladoId;
    const descricao = String(new FormData(form).get("descricao") || "").trim();
    const disciplinasIds = getSelectedSimuladoEditSubjectIds();
    const feedback = document.getElementById("simuladoEditFormFeedback");

    feedback.innerHTML = message("Salvando alterações...");

    try {
        await requestJson(`/simulados/${encodeURIComponent(simuladoId)}`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ descricao, disciplinasIds })
        });
        await loadSimulationsView();
        showSimulationsList(message("Simulado atualizado com sucesso.", "ok"));
    } catch (error) {
        feedback.innerHTML = message(error.message || "Erro ao salvar simulado.", "error");
    }
}

function handleSimulationTableClick(event) {
    const button = event.target.closest("[data-simulation-action]");
    if (!button) {
        return;
    }

    if (button.dataset.simulationAction === "details") {
        showSimulationDetails(button.dataset.simulationId);
    } else if (button.dataset.simulationAction === "edit") {
        showSimulationEditForm(button.dataset.simulationId);
    }
}

function correctionStatusInfo(status) {
    const normalized = String(status || "").toUpperCase();
    if (normalized === "EM_ANALISE") {
        return { label: "Em Análise", className: "warning", open: true };
    }
    if (normalized === "APROVADA") {
        return { label: "Aprovada", className: "success", open: false };
    }
    if (normalized === "REPROVADA") {
        return { label: "Reprovada", className: "danger", open: false };
    }
    return { label: "Pendente", className: "neutral", open: true };
}

function renderCorrectionStatusBadge(status) {
    const info = correctionStatusInfo(status);
    return `<span class="status-pill ${info.className}">${escapeHtml(info.label)}</span>`;
}

function showCorrectionsList(feedback = "") {
    document.getElementById("correctionsListView").hidden = false;
    document.getElementById("correctionReviewView").hidden = true;
    document.getElementById("correctionsFeedback").innerHTML = feedback;
    renderCorrectionsTable();
}

function renderCorrectionsTable() {
    const tbody = document.getElementById("correctionsTableBody");
    if (state.retificacoes.length === 0) {
        tbody.innerHTML = `<tr><td colspan="5" class="muted">Nenhuma solicitação de retificação cadastrada.</td></tr>`;
        return;
    }

    tbody.innerHTML = state.retificacoes.map((retificacao) => {
        const statusInfo = correctionStatusInfo(retificacao.status);
        const action = statusInfo.open ? "Analisar" : "Ver";
        return `
            <tr>
                <td>
                    <span class="row-title">${escapeHtml(retificacao.alunoNome || `Aluno ${retificacao.alunoId || "-"}`)}</span>
                    <span class="row-subtitle">Nota ID ${escapeHtml(retificacao.notaId)}</span>
                </td>
                <td>${formatNumber(retificacao.notaAtual)}</td>
                <td>${escapeHtml(retificacao.justificativa || "-")}</td>
                <td>${renderCorrectionStatusBadge(retificacao.status)}</td>
                <td>
                    <button class="table-action" type="button" data-correction-action="${statusInfo.open ? "review" : "view"}" data-correction-id="${escapeHtml(retificacao.id)}">${action}</button>
                </td>
            </tr>
        `;
    }).join("");
}

async function loadCorrectionsView() {
    const feedback = document.getElementById("correctionsFeedback");
    feedback.innerHTML = message("Carregando retificações...");

    try {
        state.retificacoes = await requestJson("/retificacoes");
        renderCorrectionsTable();
        feedback.innerHTML = "";
    } catch (error) {
        state.retificacoes = [];
        renderCorrectionsTable();
        feedback.innerHTML = message(error.message || "Não foi possível carregar as retificações.", "error");
    }
}

function renderCorrectionReviewInfo(retificacao) {
    document.getElementById("correctionReviewInfo").innerHTML = `
        <span><strong>Aluno</strong><small>${escapeHtml(retificacao.alunoNome || "-")}</small></span>
        <span><strong>Disciplina</strong><small>${escapeHtml(retificacao.disciplinaNome || "-")}</small></span>
        <span><strong>Simulado</strong><small>${escapeHtml(retificacao.simuladoDescricao || "-")}</small></span>
        <span><strong>Nota Atual</strong><small>${formatNumber(retificacao.notaAtual)}</small></span>
        <span class="correction-full-row"><strong>Justificativa do Aluno</strong><small>${escapeHtml(retificacao.justificativa || "-")}</small></span>
    `;
}

function renderCorrectionReview(retificacao, readOnly = false) {
    state.selectedRetificacao = retificacao;
    document.getElementById("correctionsListView").hidden = true;
    document.getElementById("correctionReviewView").hidden = false;
    document.getElementById("correctionReviewStatus").textContent = correctionStatusInfo(retificacao.status).label;
    renderCorrectionReviewInfo(retificacao);

    const form = document.getElementById("correctionDecisionForm");
    form.reset();
    document.getElementById("correctionDecisionFeedback").innerHTML = readOnly && retificacao.justificativaDecisao
        ? message(`Decisão registrada: ${retificacao.justificativaDecisao}`)
        : "";
    document.getElementById("correctionNewGrade").disabled = readOnly;
    document.getElementById("correctionDecisionJustification").disabled = readOnly;
    document.getElementById("approveCorrectionButton").hidden = readOnly;
    document.getElementById("rejectCorrectionButton").hidden = readOnly;
}

async function returnToCorrectionsList() {
    showCorrectionsList();
    await loadCorrectionsView();
}

async function showCorrectionReview(correctionId, readOnly = false) {
    document.getElementById("correctionsFeedback").innerHTML = message("Carregando solicitação...");

    try {
        if (!readOnly) {
            const current = state.retificacoes.find((item) => String(item.id) === String(correctionId));
            if (String(current?.status || "").toUpperCase() === "PENDENTE") {
                await requestJson(`/retificacoes/${encodeURIComponent(correctionId)}/em-analise`, { method: "PATCH" });
            }
        }

        const retificacao = await requestJson(`/retificacoes/${encodeURIComponent(correctionId)}`);
        renderCorrectionReview(retificacao, readOnly || !correctionStatusInfo(retificacao.status).open);
        document.getElementById("correctionsFeedback").innerHTML = "";
    } catch (error) {
        document.getElementById("correctionsFeedback").innerHTML = message(error.message || "Não foi possível carregar a solicitação.", "error");
    }
}

function validateCorrectionDecision(shouldApprove) {
    const justification = String(document.getElementById("correctionDecisionJustification").value || "").trim();
    const gradeText = String(document.getElementById("correctionNewGrade").value || "").trim();
    const grade = Number(gradeText);

    if (shouldApprove && (!gradeText || Number.isNaN(grade) || grade < 0 || grade > 10)) {
        return "Informe uma nova nota entre 0 e 10.";
    }
    if (!justification) {
        return "Informe a justificativa da decisão.";
    }

    return "";
}

async function submitCorrectionDecision(shouldApprove) {
    const feedback = document.getElementById("correctionDecisionFeedback");
    const retificacao = state.selectedRetificacao;
    const validationMessage = validateCorrectionDecision(shouldApprove);

    if (!retificacao?.id) {
        feedback.innerHTML = message("Selecione uma solicitação para análise.", "error");
        return;
    }

    if (validationMessage) {
        feedback.innerHTML = message(validationMessage, "error");
        return;
    }

    const justification = String(document.getElementById("correctionDecisionJustification").value || "").trim();
    feedback.innerHTML = message(shouldApprove ? "Aprovando retificação..." : "Reprovando retificação...");

    try {
        await requestJson(`/retificacoes/${encodeURIComponent(retificacao.id)}/${shouldApprove ? "aprovar" : "reprovar"}`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(shouldApprove
                ? {
                    novoValorNota: Number(document.getElementById("correctionNewGrade").value),
                    justificativaDecisao: justification
                }
                : { justificativaDecisao: justification })
        });

        showCorrectionsList(message("Atualizando solicitações..."));
        await loadCorrectionsView();
        document.getElementById("correctionsFeedback").innerHTML = message(shouldApprove ? "Retificação aprovada com sucesso." : "Retificação reprovada com sucesso.", "ok");
    } catch (error) {
        feedback.innerHTML = message(error.message || "Não foi possível registrar a decisão.", "error");
    }
}

function handleCorrectionsTableClick(event) {
    const button = event.target.closest("[data-correction-action]");
    if (!button) {
        return;
    }

    showCorrectionReview(button.dataset.correctionId, button.dataset.correctionAction === "view");
}

function renderGuardianStatus(active) {
    const label = active ? "Ativo" : "Inativo";
    const className = active ? "active" : "inactive";
    return `<span class="student-status ${className}">${label}</span>`;
}

function guardianPermissionsText(responsavel) {
    const permissions = [
        responsavel?.podeVisualizarNotas ? "Notas" : null,
        responsavel?.podeVisualizarSimulados ? "Simulados" : null,
        responsavel?.podeVisualizarDesempenho ? "Desempenho" : null
    ].filter(Boolean);

    return permissions.length > 0 ? permissions.join(", ") : "Nenhuma permissão";
}

function buildGuardianRows() {
    return state.responsaveis.map((responsavel) => {
        const aluno = state.alunos.find((item) => String(item.responsavelId) === String(responsavel.id));
        return {
            id: responsavel.id,
            nome: responsavel.nome || "-",
            email: responsavel.email || "-",
            alunoId: aluno?.id || null,
            alunoNome: aluno?.nome || "Sem aluno vinculado",
            vinculoAtivo: Boolean(aluno?.vinculoResponsavelAtivo),
            podeVisualizarNotas: Boolean(aluno?.podeVisualizarNotas),
            podeVisualizarSimulados: Boolean(aluno?.podeVisualizarSimulados),
            podeVisualizarDesempenho: Boolean(aluno?.podeVisualizarDesempenho)
        };
    });
}

function renderGuardiansTable() {
    const tbody = document.getElementById("guardiansTableBody");
    if (state.responsaveisLinhas.length === 0) {
        tbody.innerHTML = `<tr><td colspan="5" class="muted">Nenhum responsável cadastrado.</td></tr>`;
        return;
    }

    tbody.innerHTML = state.responsaveisLinhas.map((responsavel) => {
        const toggle = responsavel.alunoId
            ? responsavel.vinculoAtivo
                ? `<button class="table-action danger" type="button" data-guardian-action="inactive" data-guardian-id="${escapeHtml(responsavel.id)}">Inativar</button>`
                : `<button class="table-action success" type="button" data-guardian-action="active" data-guardian-id="${escapeHtml(responsavel.id)}">Ativar</button>`
            : "";

        return `
            <tr>
                <td>${escapeHtml(responsavel.nome)}</td>
                <td>${escapeHtml(responsavel.email)}</td>
                <td>${escapeHtml(responsavel.alunoNome)}</td>
                <td>${responsavel.alunoId ? renderGuardianStatus(responsavel.vinculoAtivo) : `<span class="muted">Sem vínculo</span>`}</td>
                <td>
                    <div class="table-actions">
                        <button class="table-action" type="button" data-guardian-action="details" data-guardian-id="${escapeHtml(responsavel.id)}">Detalhes</button>
                        ${toggle}
                        <button class="table-action danger" type="button" data-guardian-action="delete" data-guardian-id="${escapeHtml(responsavel.id)}">Excluir</button>
                    </div>
                </td>
            </tr>
        `;
    }).join("");
}

function showGuardiansList(feedback = "") {
    document.getElementById("guardiansListView").hidden = false;
    document.getElementById("guardianLinkView").hidden = true;
    document.getElementById("guardianFormView").hidden = true;
    document.getElementById("guardianLinkFeedback").innerHTML = "";
    document.getElementById("guardianFormFeedback").innerHTML = "";
    document.getElementById("guardiansFeedback").innerHTML = feedback;
    renderGuardiansTable();
}

async function loadGuardiansView() {
    const feedback = document.getElementById("guardiansFeedback");
    feedback.innerHTML = message("Carregando responsáveis...");

    try {
        const [responsaveis, alunos] = await Promise.all([
            requestJson("/responsaveis"),
            requestJson("/alunos")
        ]);
        state.responsaveis = responsaveis;
        state.alunos = alunos;
        state.responsaveisLinhas = buildGuardianRows();
        feedback.innerHTML = "";
        renderGuardiansTable();
    } catch (error) {
        state.responsaveis = [];
        state.responsaveisLinhas = [];
        renderGuardiansTable();
        feedback.innerHTML = message("Não foi possível carregar os responsáveis. Tente novamente em alguns instantes.", "error");
    }
}

function renderGuardianSelects() {
    const studentSelect = document.getElementById("guardianStudentSelect");
    const guardianSelect = document.getElementById("guardianSelect");

    studentSelect.innerHTML = state.alunos.length === 0
        ? `<option value="" disabled selected>Nenhum aluno cadastrado</option>`
        : `<option value="">Selecione o aluno</option>${state.alunos.map((aluno) =>
            `<option value="${escapeHtml(aluno.id)}">${escapeHtml(aluno.nome || `Aluno ${aluno.id}`)}</option>`
        ).join("")}`;

    guardianSelect.innerHTML = state.responsaveis.length === 0
        ? `<option value="" disabled selected>Nenhum responsável cadastrado</option>`
        : `<option value="">Selecione o responsável</option>${state.responsaveis.map((responsavel) =>
            `<option value="${escapeHtml(responsavel.id)}">${escapeHtml(responsavel.nome || `Responsável ${responsavel.id}`)}</option>`
        ).join("")}`;
}

async function showGuardianLinkForm() {
    document.getElementById("guardiansListView").hidden = true;
    document.getElementById("guardianLinkView").hidden = false;
    document.getElementById("guardianFormView").hidden = true;
    document.getElementById("guardianLinkForm").reset();
    document.getElementById("guardianLinkFeedback").innerHTML = message("Carregando dados...");

    try {
        const [responsaveis, alunos] = await Promise.all([
            requestJson("/responsaveis"),
            requestJson("/alunos")
        ]);
        state.responsaveis = responsaveis;
        state.alunos = alunos;
        renderGuardianSelects();
        document.getElementById("guardianLinkFeedback").innerHTML = "";
    } catch (error) {
        state.responsaveis = [];
        state.alunos = [];
        renderGuardianSelects();
        document.getElementById("guardianLinkFeedback").innerHTML = message(error.message, "error");
    }
}

function showGuardianForm() {
    document.getElementById("guardiansListView").hidden = true;
    document.getElementById("guardianLinkView").hidden = true;
    document.getElementById("guardianFormView").hidden = false;
    document.getElementById("guardianForm").reset();
    document.getElementById("guardianFormFeedback").innerHTML = "";
}

function closeGuardianDetails() {
    document.getElementById("guardianDetailsCard").hidden = true;
    document.getElementById("guardianDetailsContent").innerHTML = "";
}

function showGuardianDetails(responsavelId) {
    const responsavel = state.responsaveisLinhas.find((item) => String(item.id) === String(responsavelId));
    if (!responsavel) {
        return;
    }

    document.getElementById("guardianDetailsCard").hidden = false;
    document.getElementById("guardianDetailsContent").innerHTML = `
        <div class="subject-details-grid">
            <span><strong>Nome</strong><small>${escapeHtml(responsavel.nome)}</small></span>
            <span><strong>Email</strong><small>${escapeHtml(responsavel.email)}</small></span>
            <span><strong>Aluno vinculado</strong><small>${escapeHtml(responsavel.alunoNome)}</small></span>
            <span><strong>Status do vínculo</strong>${responsavel.alunoId ? renderGuardianStatus(responsavel.vinculoAtivo) : `<small>Sem vínculo</small>`}</span>
            <span><strong>Permissões</strong><small>${escapeHtml(guardianPermissionsText(responsavel))}</small></span>
        </div>
    `;
}

async function toggleGuardianLink(responsavelId, shouldActivate) {
    const responsavel = state.responsaveisLinhas.find((item) => String(item.id) === String(responsavelId));
    if (!responsavel?.alunoId) {
        return;
    }

    const feedback = document.getElementById("guardiansFeedback");
    feedback.innerHTML = message(shouldActivate ? "Ativando vínculo..." : "Inativando vínculo...");

    try {
        if (shouldActivate) {
            await requestJson(`/alunos/${encodeURIComponent(responsavel.alunoId)}/responsavel`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    responsavelId: responsavel.id,
                    podeVisualizarNotas: true,
                    podeVisualizarSimulados: true,
                    podeVisualizarDesempenho: true
                })
            });
        } else {
            await requestJson(`/alunos/${encodeURIComponent(responsavel.alunoId)}/responsavel`, { method: "DELETE" });
        }
        closeGuardianDetails();
        await loadGuardiansView();
    } catch (error) {
        feedback.innerHTML = message(error.message, "error");
    }
}

async function deleteGuardian(responsavelId) {
    const responsavel = state.responsaveisLinhas.find((item) => String(item.id) === String(responsavelId));
    const confirmed = window.confirm("Deseja excluir este responsável definitivamente? Vínculos com alunos serão removidos.");
    if (!confirmed) {
        return;
    }

    const feedback = document.getElementById("guardiansFeedback");
    feedback.innerHTML = message(`Excluindo ${responsavel?.nome || "responsável"}...`);

    try {
        await requestJson(`/responsaveis/${encodeURIComponent(responsavelId)}`, { method: "DELETE" });
        closeGuardianDetails();
        await loadGuardiansView();
        document.getElementById("guardiansFeedback").innerHTML = message("Responsável excluído com sucesso.", "ok");
    } catch (error) {
        feedback.innerHTML = message(error.message, "error");
    }
}

async function handleGuardianLinkSubmit(event) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const alunoId = formData.get("alunoId");
    const responsavelId = formData.get("responsavelId");
    const feedback = document.getElementById("guardianLinkFeedback");
    feedback.innerHTML = message("Vinculando responsável...");

    try {
        await requestJson(`/alunos/${encodeURIComponent(alunoId)}/responsavel`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                responsavelId: Number(responsavelId),
                podeVisualizarNotas: formData.has("podeVisualizarNotas"),
                podeVisualizarSimulados: formData.has("podeVisualizarSimulados"),
                podeVisualizarDesempenho: formData.has("podeVisualizarDesempenho")
            })
        });
        showGuardiansList();
        await loadGuardiansView();
        document.getElementById("guardiansFeedback").innerHTML = message("Responsável vinculado com sucesso.", "ok");
    } catch (error) {
        feedback.innerHTML = message(error.message, "error");
    }
}

async function handleGuardianSubmit(event) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const feedback = document.getElementById("guardianFormFeedback");
    feedback.innerHTML = message("Salvando responsável...");

    try {
        await requestJson("/responsaveis", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                nome: String(formData.get("nome") || "").trim(),
                email: String(formData.get("email") || "").trim()
            })
        });
        showGuardiansList();
        await loadGuardiansView();
        document.getElementById("guardiansFeedback").innerHTML = message("Responsável cadastrado com sucesso.", "ok");
    } catch (error) {
        feedback.innerHTML = message(error.message, "error");
    }
}

function handleGuardianTableClick(event) {
    const button = event.target.closest("[data-guardian-action]");
    if (!button) {
        return;
    }

    const responsavelId = button.dataset.guardianId;
    const action = button.dataset.guardianAction;
    if (action === "details") {
        showGuardianDetails(responsavelId);
    } else if (action === "delete") {
        deleteGuardian(responsavelId);
    } else if (action === "active" || action === "inactive") {
        toggleGuardianLink(responsavelId, action === "active");
    }
}

document.querySelectorAll(".nav-link").forEach((link) => {
    link.addEventListener("click", () => showSection(link.dataset.section));
});

document.getElementById("loginForm").addEventListener("submit", handleLoginSubmit);
document.getElementById("changeProfileButton").addEventListener("click", handleChangeProfile);
document.getElementById("notificationsForm").addEventListener("submit", handleNotificationsSubmit);
document.getElementById("notificationsList").addEventListener("click", handleNotificationsClick);
document.getElementById("portalForm").addEventListener("submit", handlePortalSubmit);
document.getElementById("newStudentButton").addEventListener("click", showStudentForm);
document.getElementById("cancelStudentButton").addEventListener("click", () => showStudentsList());
document.getElementById("studentForm").addEventListener("submit", handleStudentSubmit);
document.getElementById("cancelStudentEditButton").addEventListener("click", () => showStudentsList());
document.getElementById("studentEditForm").addEventListener("submit", handleStudentEditSubmit);
document.getElementById("studentsTableBody").addEventListener("click", handleStudentsTableClick);
document.getElementById("newClassButton").addEventListener("click", showClassForm);
document.getElementById("cancelClassButton").addEventListener("click", () => showClassesList());
document.getElementById("classForm").addEventListener("submit", handleClassSubmit);
document.getElementById("newSubjectButton").addEventListener("click", showSubjectForm);
document.getElementById("cancelSubjectButton").addEventListener("click", () => showSubjectsList());
document.getElementById("closeSubjectDetailsButton").addEventListener("click", closeSubjectDetails);
document.getElementById("subjectForm").addEventListener("submit", handleSubjectSubmit);
document.getElementById("cancelSubjectEditButton").addEventListener("click", () => showSubjectsList());
document.getElementById("subjectEditForm").addEventListener("submit", handleSubjectEditSubmit);
document.getElementById("subjectsTableBody").addEventListener("click", handleSubjectTableClick);
document.getElementById("noteForm").addEventListener("submit", handleNoteSubmit);
document.getElementById("noteExamSelect").addEventListener("change", handleNoteExamChange);
document.getElementById("studentNotesForm").addEventListener("submit", handleStudentNotesSubmit);
document.getElementById("clearNoteButton").addEventListener("click", () => clearNoteForm());
document.getElementById("performanceForm").addEventListener("submit", handlePerformanceSubmit);
document.getElementById("changePerformanceStudentButton").addEventListener("click", handleChangePerformanceStudent);
document.getElementById("newSimulationButton").addEventListener("click", showSimulationCreateView);
document.getElementById("cancelSimulationButton").addEventListener("click", () => showSimulationsList());
document.getElementById("backToSimulationsButton").addEventListener("click", () => showSimulationsList());
document.getElementById("simulationForm").addEventListener("submit", handleSimulationSubmit);
document.getElementById("simulationSubjectOptions").addEventListener("change", updateSimulationSelectedCounter);
document.getElementById("simulationsTableBody").addEventListener("click", handleSimulationTableClick);
document.getElementById("cancelSimuladoEditButton").addEventListener("click", () => showSimulationsList());
document.getElementById("simuladoEditForm").addEventListener("submit", handleSimuladoEditSubmit);
document.getElementById("simuladoEditSubjectOptions").addEventListener("change", updateSimuladoEditCounter);
document.getElementById("backToCorrectionsButton").addEventListener("click", returnToCorrectionsList);
document.getElementById("approveCorrectionButton").addEventListener("click", () => submitCorrectionDecision(true));
document.getElementById("rejectCorrectionButton").addEventListener("click", () => submitCorrectionDecision(false));
document.getElementById("correctionsTableBody").addEventListener("click", handleCorrectionsTableClick);
document.getElementById("linkGuardianButton").addEventListener("click", showGuardianLinkForm);
document.getElementById("newGuardianButton").addEventListener("click", showGuardianForm);
document.getElementById("cancelGuardianLinkButton").addEventListener("click", () => showGuardiansList());
document.getElementById("cancelGuardianButton").addEventListener("click", () => showGuardiansList());
document.getElementById("closeGuardianDetailsButton").addEventListener("click", closeGuardianDetails);
document.getElementById("guardianLinkForm").addEventListener("submit", handleGuardianLinkSubmit);
document.getElementById("guardianForm").addEventListener("submit", handleGuardianSubmit);
document.getElementById("guardiansTableBody").addEventListener("click", handleGuardianTableClick);

showLogin();
