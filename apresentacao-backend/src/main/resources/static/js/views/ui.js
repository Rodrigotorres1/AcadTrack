import { escapeHtml } from "../utils.js";

export function setStatus(elementId, text, kind = "neutral") {
    const element = document.getElementById(elementId);
    if (!element) return;

    element.textContent = text;
    element.className = `status-pill ${kind}`;
}

export function message(text, kind = "neutral") {
    const className = kind === "error" ? "message-box error" : kind === "ok" ? "message-box ok" : "message-box";
    return `<div class="${className}">${escapeHtml(text)}</div>`;
}

export function setFeedback(target, html) {
    const element = typeof target === "string" ? document.getElementById(target) : target;
    if (element) {
        element.innerHTML = html;
    }
}
