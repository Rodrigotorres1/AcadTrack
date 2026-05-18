function createInitialState() {
    return {
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
        retificacaoNotas: [],
        selectedRetificacao: null,
        notificationsResponsavelId: null,
        responsaveis: [],
        responsaveisLinhas: []
    };
}

export const state = createInitialState();

export function setState(key, value) {
    if (!Object.hasOwn(state, key)) {
        throw new Error(`Estado desconhecido: ${key}`);
    }
    state[key] = value;
    return value;
}

export function patchState(values) {
    Object.entries(values).forEach(([key, value]) => setState(key, value));
    return state;
}

export function resetSession() {
    patchState({
        perfil: null,
        alunoMatricula: null
    });
}

export function startProfileSession(profile) {
    patchState({
        perfil: profile,
        alunoMatricula: null
    });
}

export function setStudentEnrollment(value) {
    return setState("alunoMatricula", String(value || "").trim() || null);
}
