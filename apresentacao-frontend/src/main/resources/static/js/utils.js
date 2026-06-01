export const formatNumber = (value) => {
    if (value === null || value === undefined || Number.isNaN(Number(value))) {
        return "-";
    }
    return Number(value).toLocaleString("pt-BR", {
        minimumFractionDigits: 1,
        maximumFractionDigits: 2
    });
};

export const normalize = (value) => String(value || "").toLowerCase();

export const normalizeClassName = (value) => String(value || "")
    .trim()
    .toLowerCase()
    .replaceAll("°", "º")
    .replace(/\s+/g, "");

export const riskClass = (nivelRisco) => {
    const risk = normalize(nivelRisco);
    if (risk.includes("alto")) return "alto";
    if (risk.includes("moderado")) return "moderado";
    if (risk.includes("baixo")) return "baixo";
    return "neutral";
};

export const statusClass = (value) => {
    const text = normalize(value);
    if (text.includes("alto") || text.includes("erro") || text.includes("bloque")) return "danger";
    if (text.includes("moderado") || text.includes("atenc") || text.includes("media")) return "warning";
    if (text.includes("baixo") || text.includes("sucesso") || text.includes("ok")) return "success";
    return "neutral";
};

export const escapeHtml = (value) => String(value ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");

export function isPositiveEnrollment(value) {
    return /^\d+$/.test(String(value || "").trim()) && Number(value) >= 1;
}
