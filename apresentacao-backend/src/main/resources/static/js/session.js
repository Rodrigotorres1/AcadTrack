import { getProfileConfig } from "./config.js";
import { resetSession, setStudentEnrollment, startProfileSession } from "./store.js";

export function renderActiveProfile(profile) {
    const config = getProfileConfig(profile);
    document.getElementById("activeProfileLabel").textContent = config?.label || "-";
    document.getElementById("activeProfileDescription").textContent = config?.description || "";
}

export function rememberStudentEnrollment(value) {
    return setStudentEnrollment(value);
}

export function getStudentEnrollmentFromForm(form) {
    return String(new FormData(form).get("alunoId") || "").trim();
}

export function startSession(profile) {
    const config = getProfileConfig(profile);
    if (!config) {
        return null;
    }

    startProfileSession(profile);
    return config;
}

export function endSession() {
    resetSession();
}
