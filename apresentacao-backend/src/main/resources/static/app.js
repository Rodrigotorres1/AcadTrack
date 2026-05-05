const state = {
    dashboardLoaded: false,
    currentReport: null,
    alunos: [],
    turmas: [],
    disciplinas: [],
    responsaveis: [],
    responsaveisLinhas: []
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

async function requestJson(path, options = {}) {
    const response = await fetch(path, {
        ...options,
        headers: {
            "Accept": "application/json",
            ...(options.headers || {})
        }
    });

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

function showSection(sectionId) {
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

    if (sectionId === "disciplinas") {
        showSubjectsList();
        loadSubjects();
    }

    if (sectionId === "responsaveis") {
        showGuardiansList();
        loadGuardiansView();
    }
}

function renderRiskPill(nivelRisco) {
    return `<span class="risk-pill ${riskClass(nivelRisco)}">${escapeHtml(nivelRisco || "-")}</span>`;
}

function renderReportTable(report) {
    const tbody = document.getElementById("reportTableBody");
    const alunos = report.alunos || [];

    if (alunos.length === 0) {
        tbody.innerHTML = `<tr><td colspan="5" class="muted">Nenhum aluno com notas foi retornado pela API.</td></tr>`;
        return;
    }

    tbody.innerHTML = alunos.map((aluno) => `
        <tr>
            <td>
                <span class="row-title">${escapeHtml(aluno.nomeAluno || `Aluno ${aluno.alunoId}`)}</span>
                <span class="row-subtitle">ID ${escapeHtml(aluno.alunoId)}</span>
            </td>
            <td>${formatNumber(aluno.mediaGeral)}</td>
            <td>${renderRiskPill(aluno.nivelRisco)}</td>
            <td>${escapeHtml(aluno.situacaoAcademica || "-")}</td>
            <td>${escapeHtml(aluno.tipoPlanoEstudo || "-")}</td>
        </tr>
    `).join("");
}

function renderDashboard(report) {
    const alunos = report.alunos || [];
    document.getElementById("metricTotal").textContent = report.totalAlunosAnalisados ?? alunos.length;
    document.getElementById("metricAlto").textContent = alunos.filter((aluno) => riskClass(aluno.nivelRisco) === "alto").length;
    document.getElementById("metricModerado").textContent = alunos.filter((aluno) => riskClass(aluno.nivelRisco) === "moderado").length;
    document.getElementById("metricBaixo").textContent = alunos.filter((aluno) => riskClass(aluno.nivelRisco) === "baixo").length;

    const dashboardList = document.getElementById("dashboardList");
    const destaques = alunos.slice(0, 5);
    dashboardList.innerHTML = destaques.length === 0
        ? message("Nenhum aluno com notas foi retornado pela API.")
        : destaques.map((aluno) => `
            <div class="compact-row">
                <span>
                    <span class="row-title">${escapeHtml(aluno.nomeAluno || `Aluno ${aluno.alunoId}`)}</span>
                    <span class="row-subtitle">MÃ©dia ${formatNumber(aluno.mediaGeral)} | ${escapeHtml(aluno.situacaoAcademica || "-")}</span>
                </span>
                ${renderRiskPill(aluno.nivelRisco)}
                <span class="muted">${escapeHtml(aluno.tipoPlanoEstudo || "-")}</span>
            </div>
        `).join("");
}

async function loadReport(ordenacao = "MAIOR_RISCO", updateDashboard = false) {
    setStatus("reportStatus", "Carregando", "neutral");
    const report = await requestJson(`/relatorios/desempenho-academico?ordenacao=${encodeURIComponent(ordenacao)}`);
    state.currentReport = report;
    renderReportTable(report);
    document.getElementById("reportTitle").textContent = `${report.totalAlunosAnalisados ?? 0} alunos analisados`;
    setStatus("reportStatus", report.ordenacao || ordenacao, "success");

    if (updateDashboard) {
        renderDashboard(report);
        state.dashboardLoaded = true;
        setStatus("dashboardStatus", "Atualizado", "success");
    }
}

async function loadDashboard() {
    setStatus("dashboardStatus", "Carregando", "neutral");
    try {
        const report = await requestJson("/relatorios/desempenho-academico?ordenacao=MAIOR_RISCO");
        renderDashboard(report);
        state.dashboardLoaded = true;
        setStatus("dashboardStatus", "Atualizado", "success");
    } catch (error) {
        document.getElementById("dashboardList").innerHTML = message(error.message, "error");
        setStatus("dashboardStatus", "Erro", "danger");
    }
}

async function handleReportLoad() {
    const ordenacao = document.getElementById("ordenacaoSelect").value;
    try {
        await loadReport(ordenacao, ordenacao === "MAIOR_RISCO");
    } catch (error) {
        document.getElementById("reportTableBody").innerHTML = `<tr><td colspan="5">${message(error.message, "error")}</td></tr>`;
        setStatus("reportStatus", "Erro", "danger");
    }
}

function renderPlan(plan) {
    const risk = riskClass(plan.nivelRisco);
    return `
        <article class="card">
            <div class="plan-layout">
                <div class="plan-stat">
                    ${renderRiskPill(plan.nivelRisco)}
                    <strong class="text-${risk === "alto" ? "danger" : risk === "moderado" ? "warning" : "success"}">${formatNumber(plan.mediaGeral)}</strong>
                    <span class="muted">Aluno ID ${escapeHtml(plan.alunoId)}</span>
                </div>
                <div>
                    <h3>${escapeHtml(plan.tipoPlano || "-")}</h3>
                    <p class="muted">${escapeHtml(plan.descricao || "")}</p>
                    <p><strong>Carga semanal sugerida:</strong> ${escapeHtml(plan.cargaHorariaSemanalSugerida ?? "-")}h</p>
                    <ul class="orientation-list">
                        ${(plan.orientacoes || []).map((orientacao) => `<li>${escapeHtml(orientacao)}</li>`).join("")}
                    </ul>
                </div>
            </div>
        </article>
    `;
}

async function handlePlanSubmit(event) {
    event.preventDefault();
    const alunoId = new FormData(event.currentTarget).get("alunoId");
    const result = document.getElementById("planResult");
    result.className = "card empty-state";
    result.innerHTML = "Consultando recomendaÃ§Ã£o...";

    try {
        const plan = await requestJson(`/alunos/${encodeURIComponent(alunoId)}/plano-estudo`);
        result.outerHTML = `<div id="planResult">${renderPlan(plan)}</div>`;
    } catch (error) {
        result.innerHTML = message(error.message, "error");
    }
}

function renderNotifications(notifications) {
    if (!notifications || notifications.length === 0) {
        return message("Nenhuma notificaÃ§Ã£o encontrada para este responsÃ¡vel.");
    }

    return notifications.map((notificacao) => {
        const prioridade = notificacao.prioridade || "-";
        return `
            <article class="notification-card">
                <div class="card-title-row">
                    <h3>Aluno ${escapeHtml(notificacao.alunoId)}</h3>
                    <span class="status-pill ${statusClass(prioridade)}">${escapeHtml(prioridade)}</span>
                </div>
                <p>${escapeHtml(notificacao.mensagem || "")}</p>
                <div class="notification-meta">
                    <span>Risco: ${escapeHtml(notificacao.nivelRisco || "-")}</span>
                    <span>Status: ${escapeHtml(notificacao.status || "-")}</span>
                    <span>Criada em: ${escapeHtml(notificacao.dataCriacao || "-")}</span>
                </div>
            </article>
        `;
    }).join("");
}

async function handleNotificationsSubmit(event) {
    event.preventDefault();
    const responsavelId = new FormData(event.currentTarget).get("responsavelId");
    const list = document.getElementById("notificationsList");
    list.innerHTML = message("Carregando notificaÃ§Ãµes...");

    try {
        const notifications = await requestJson(`/responsaveis/${encodeURIComponent(responsavelId)}/notificacoes`);
        list.innerHTML = renderNotifications(notifications);
    } catch (error) {
        list.innerHTML = message(error.message, "error");
    }
}

function renderPortalSuccess(desempenho, notas, simulados) {
    return `
        <article class="card">
            <h3>Desempenho</h3>
            <p class="muted">${escapeHtml(desempenho.alerta || "Consulta autorizada pelo backend.")}</p>
            <div class="portal-meta">
                <span>MÃ©dia: ${formatNumber(desempenho.mediaGeral)}</span>
                <span>Total notas: ${escapeHtml(desempenho.totalNotas ?? "-")}</span>
                <span>Risco: ${escapeHtml(desempenho.nivelRisco || "-")}</span>
            </div>
        </article>
        <article class="card">
            <h3>Notas</h3>
            <p class="muted">${escapeHtml(notas.length)} registros retornados pela API.</p>
            <div class="compact-list">
                ${notas.slice(0, 4).map((nota) => `
                    <div class="compact-row">
                        <span>Disciplina ${escapeHtml(nota.disciplinaId ?? "-")}</span>
                        <strong>${formatNumber(nota.valor)}</strong>
                    </div>
                `).join("") || `<span class="muted">Sem notas visÃ­veis.</span>`}
            </div>
        </article>
        <article class="card">
            <h3>Simulados</h3>
            <p class="muted">${escapeHtml(simulados.length)} simulados disponÃ­veis.</p>
            <div class="compact-list">
                ${simulados.slice(0, 6).map((simuladoId) => `
                    <div class="compact-row">
                        <span>Simulado</span>
                        <strong>${escapeHtml(simuladoId)}</strong>
                    </div>
                `).join("") || `<span class="muted">Sem simulados visÃ­veis.</span>`}
            </div>
        </article>
    `;
}

async function handlePortalSubmit(event) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const responsavelId = formData.get("responsavelId");
    const alunoId = formData.get("alunoId");
    const result = document.getElementById("portalResult");
    result.innerHTML = message("Validando acesso no backend...");

    try {
        const base = `/responsaveis/${encodeURIComponent(responsavelId)}/alunos/${encodeURIComponent(alunoId)}`;
        const [desempenho, notas, simulados] = await Promise.all([
            requestJson(`${base}/desempenho`),
            requestJson(`${base}/notas`),
            requestJson(`${base}/simulados`)
        ]);
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
        nome: aluno.nome || "-",
        email: aluno.email || "-",
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

function renderStudentsTable() {
    const tbody = document.getElementById("studentsTableBody");
    if (state.alunos.length === 0) {
        tbody.innerHTML = `<tr><td colspan="4" class="muted">Nenhum aluno cadastrado.</td></tr>`;
        return;
    }

    tbody.innerHTML = state.alunos.map((aluno) => `
        <tr>
            <td>${escapeHtml(aluno.nome)}</td>
            <td>${escapeHtml(aluno.email)}</td>
            <td>${escapeHtml(aluno.turma)}</td>
            <td>${renderStudentStatus(aluno.situacao)}</td>
        </tr>
    `).join("");
}

function showStudentsList(feedback = "") {
    document.getElementById("studentsListView").hidden = false;
    document.getElementById("studentFormView").hidden = true;
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

async function loadClasses(showFeedback = false) {
    const feedback = document.getElementById("studentFormFeedback");
    if (showFeedback && feedback) {
        feedback.innerHTML = message("Carregando turmas...");
    }

    try {
        const turmas = await requestJson("/turmas");
        state.turmas = turmas;
        renderClassOptions();
        if (showFeedback && feedback) {
            feedback.innerHTML = "";
        }
    } catch (error) {
        state.turmas = [];
        renderClassOptions();
        if (showFeedback && feedback) {
            feedback.innerHTML = message("Não foi possível carregar as turmas. Tente novamente em alguns instantes.", "error");
        }
    }
}

async function loadStudentsView() {
    await loadClasses(false);
    await loadStudents();
}

async function showStudentForm() {
    document.getElementById("studentsListView").hidden = true;
    document.getElementById("studentFormView").hidden = false;
    document.getElementById("studentForm").reset();
    document.getElementById("studentFormFeedback").innerHTML = "";
    renderClassOptions();
    await loadClasses(true);
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

        await requestJson(`/alunos/${encodeURIComponent(aluno.id)}/turma`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ turmaId: Number(turmaId) })
        });

        showStudentsList();
        await loadStudentsView();
        document.getElementById("studentsFeedback").innerHTML = message("Aluno cadastrado com sucesso.", "ok");
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
                        ${action}
                        <button class="table-action danger" type="button" data-subject-action="delete" data-subject-id="${escapeHtml(disciplina.id)}">Excluir</button>
                    </div>
                </td>
            </tr>
        `;
    }).join("");
}

function showSubjectsList(feedback = "") {
    document.getElementById("subjectsListView").hidden = false;
    document.getElementById("subjectFormView").hidden = true;
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
                : `/disciplinas/${encodeURIComponent(disciplinaId)}`,
            { method: shouldActivate ? "PATCH" : "DELETE" }
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
        await requestJson(`/disciplinas/${encodeURIComponent(disciplinaId)}/excluir`, {
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

function handleSubjectTableClick(event) {
    const button = event.target.closest("[data-subject-action]");
    if (!button) {
        return;
    }

    const disciplinaId = button.dataset.subjectId;
    const action = button.dataset.subjectAction;
    if (action === "details") {
        showSubjectDetails(disciplinaId);
    } else if (action === "delete") {
        deleteSubject(disciplinaId);
    } else {
        updateSubjectStatus(disciplinaId, action === "active");
    }
}

function renderGuardianStatus(active) {
    const label = active ? "Ativo" : "Inativo";
    const className = active ? "active" : "inactive";
    return `<span class="student-status ${className}">${label}</span>`;
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
                        <button class="table-action" type="button" data-guardian-action="portal" data-guardian-id="${escapeHtml(responsavel.id)}">Ver como responsável</button>
                        <button class="table-action" type="button" data-guardian-action="details" data-guardian-id="${escapeHtml(responsavel.id)}">Detalhes</button>
                        ${toggle}
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
        </div>
    `;
}

function openPortalForGuardian(responsavelId) {
    const responsavel = state.responsaveisLinhas.find((item) => String(item.id) === String(responsavelId));
    showSection("portal");
    document.getElementById("portalResponsavelId").value = responsavel?.id || responsavelId;
    document.getElementById("portalAlunoId").value = responsavel?.alunoId || "";
    document.getElementById("portalResult").innerHTML = responsavel?.alunoId
        ? message("Responsável selecionado. Consulte os dados em modo leitura pelo botão Consultar.")
        : message("Este responsável ainda não possui aluno vinculado.", "error");
}

function simulateBlockedAccess() {
    const inactive = state.responsaveisLinhas.find((item) => item.alunoId && !item.vinculoAtivo);
    showSection("portal");
    document.getElementById("portalResponsavelId").value = inactive?.id || "";
    document.getElementById("portalAlunoId").value = inactive?.alunoId || "";
    document.getElementById("portalResult").innerHTML = `<div class="card">${message("Acesso bloqueado: vínculo inativo simulado.", "error")}</div>`;
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
    if (action === "portal") {
        openPortalForGuardian(responsavelId);
    } else if (action === "details") {
        showGuardianDetails(responsavelId);
    } else {
        toggleGuardianLink(responsavelId, action === "active");
    }
}

document.querySelectorAll(".nav-link").forEach((link) => {
    link.addEventListener("click", () => showSection(link.dataset.section));
});

document.getElementById("refreshDashboardButton").addEventListener("click", loadDashboard);
document.getElementById("loadReportButton").addEventListener("click", handleReportLoad);
document.getElementById("planForm").addEventListener("submit", handlePlanSubmit);
document.getElementById("notificationsForm").addEventListener("submit", handleNotificationsSubmit);
document.getElementById("portalForm").addEventListener("submit", handlePortalSubmit);
document.getElementById("newStudentButton").addEventListener("click", showStudentForm);
document.getElementById("cancelStudentButton").addEventListener("click", () => showStudentsList());
document.getElementById("studentForm").addEventListener("submit", handleStudentSubmit);
document.getElementById("newSubjectButton").addEventListener("click", showSubjectForm);
document.getElementById("cancelSubjectButton").addEventListener("click", () => showSubjectsList());
document.getElementById("closeSubjectDetailsButton").addEventListener("click", closeSubjectDetails);
document.getElementById("subjectForm").addEventListener("submit", handleSubjectSubmit);
document.getElementById("subjectsTableBody").addEventListener("click", handleSubjectTableClick);
document.getElementById("linkGuardianButton").addEventListener("click", showGuardianLinkForm);
document.getElementById("newGuardianButton").addEventListener("click", showGuardianForm);
document.getElementById("cancelGuardianLinkButton").addEventListener("click", () => showGuardiansList());
document.getElementById("cancelGuardianButton").addEventListener("click", () => showGuardiansList());
document.getElementById("closeGuardianDetailsButton").addEventListener("click", closeGuardianDetails);
document.getElementById("blockedAccessButton").addEventListener("click", simulateBlockedAccess);
document.getElementById("guardianLinkForm").addEventListener("submit", handleGuardianLinkSubmit);
document.getElementById("guardianForm").addEventListener("submit", handleGuardianSubmit);
document.getElementById("guardiansTableBody").addEventListener("click", handleGuardianTableClick);

loadDashboard();
loadReport("MAIOR_RISCO").catch(() => {
    setStatus("reportStatus", "Aguardando dados", "neutral");
});

