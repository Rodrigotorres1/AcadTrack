import { getAllowedSections } from "./config.js";
import { state } from "./store.js";

export function canAccessSection(sectionId) {
    return !state.perfil || getAllowedSections(state.perfil).includes(sectionId);
}

export function applyRoleNavigation(profile) {
    const allowedSections = getAllowedSections(profile);
    document.querySelectorAll(".nav-link").forEach((link) => {
        const allowed = allowedSections.includes(link.dataset.section);
        link.hidden = !allowed;
        if (!allowed) {
            link.classList.remove("active");
        }
    });
}
