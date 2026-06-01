export const PROFILE_CONFIG = {
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

export const ROLE_SECTIONS = {
    coordenador: ["disciplinas", "simulados", "turmas", "alunos", "responsaveis"],
    professor: ["notas", "desempenho", "retificacoes"],
    aluno: ["notas", "desempenho", "retificacoes"],
    responsavel: ["portal", "notificacoes"]
};

export function getProfileConfig(profile) {
    return PROFILE_CONFIG[profile] || null;
}

export function getAllowedSections(profile) {
    return ROLE_SECTIONS[profile] || [];
}
