import { message, setFeedback } from "./views/ui.js";

export function errorMessage(error, fallback = "Não foi possível concluir a operação.") {
    return error?.message || fallback;
}

export function renderError(target, error, fallback) {
    setFeedback(target, message(errorMessage(error, fallback), "error"));
}

export async function withErrorHandling(operation, { onError, fallback } = {}) {
    try {
        return await operation();
    } catch (error) {
        if (onError) {
            onError(errorMessage(error, fallback), error);
            return null;
        }
        throw error;
    }
}
