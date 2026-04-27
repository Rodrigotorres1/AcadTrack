package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import br.com.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import br.com.acadtrack.aplicacao.nota.AnaliseDesempenhoAcademicoResultado;
import br.com.acadtrack.aplicacao.nota.AnalisarDesempenhoAcademicoUseCase;
import br.com.acadtrack.aplicacao.nota.LancarNotaUseCase;
import br.com.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import br.com.acadtrack.bdd.support.TestContext;
import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.disciplina.Disciplina;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnaliseDesempenhoSteps {

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final CriarSimuladoUseCase criarSimuladoUseCase;
    private final LancarNotaUseCase lancarNotaUseCase;
    private final AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase;

    private Aluno aluno;
    private Exception excecao;
    private AnaliseDesempenhoAcademicoResultado analise;

    public AnaliseDesempenhoSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            CriarSimuladoUseCase criarSimuladoUseCase,
            LancarNotaUseCase lancarNotaUseCase,
            AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.lancarNotaUseCase = lancarNotaUseCase;
        this.analisarDesempenhoAcademicoUseCase = analisarDesempenhoAcademicoUseCase;
    }

    @Dado("que o aluno {string} possui histórico acadêmico consistente")
    public void queOAlunoPossuiHistoricoAcademicoConsistente(String nomeAluno) {
        prepararAluno(nomeAluno);

        Disciplina matematica = criarDisciplinaUseCase.executar("Matemática " + nomeAluno);
        Disciplina portugues = criarDisciplinaUseCase.executar("Português " + nomeAluno);
        Disciplina historia = criarDisciplinaUseCase.executar("História " + nomeAluno);

        var simulado1 = criarSimuladoUseCase.executar("Simulado 1 " + nomeAluno, List.of(matematica.getId(), portugues.getId()));
        var simulado2 = criarSimuladoUseCase.executar("Simulado 2 " + nomeAluno, List.of(historia.getId()));

        lancarNotaUseCase.executar(aluno.getId(), simulado1.getId(), matematica.getId(), 8.0);
        lancarNotaUseCase.executar(aluno.getId(), simulado1.getId(), portugues.getId(), 7.5);
        lancarNotaUseCase.executar(aluno.getId(), simulado2.getId(), historia.getId(), 8.5);
    }

    @Dado("que o aluno {string} possui média geral baixa")
    public void queOAlunoPossuiMediaGeralBaixa(String nomeAluno) {
        prepararAluno(nomeAluno);

        Disciplina matematica = criarDisciplinaUseCase.executar("Matemática " + nomeAluno);
        Disciplina portugues = criarDisciplinaUseCase.executar("Português " + nomeAluno);

        var simulado = criarSimuladoUseCase.executar("Simulado baixo " + nomeAluno, List.of(matematica.getId(), portugues.getId()));

        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), matematica.getId(), 3.0);
        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), portugues.getId(), 4.0);
    }

    @Dado("que o aluno {string} possui baixo desempenho em simulado")
    public void queOAlunoPossuiBaixoDesempenhoEmSimulado(String nomeAluno) {
        prepararAluno(nomeAluno);

        Disciplina matematica = criarDisciplinaUseCase.executar("Matemática " + nomeAluno);
        Disciplina portugues = criarDisciplinaUseCase.executar("Português " + nomeAluno);
        Disciplina historia = criarDisciplinaUseCase.executar("História " + nomeAluno);

        var simuladoFraco = criarSimuladoUseCase.executar("Simulado fraco " + nomeAluno, List.of(matematica.getId()));
        var simuladoBom = criarSimuladoUseCase.executar("Simulado bom " + nomeAluno, List.of(portugues.getId(), historia.getId()));

        lancarNotaUseCase.executar(aluno.getId(), simuladoFraco.getId(), matematica.getId(), 4.0);
        lancarNotaUseCase.executar(aluno.getId(), simuladoBom.getId(), portugues.getId(), 8.0);
        lancarNotaUseCase.executar(aluno.getId(), simuladoBom.getId(), historia.getId(), 7.0);
    }

    @Dado("que o aluno {string} não possui notas lançadas")
    public void queOAlunoNaoPossuiNotasLancadas(String nomeAluno) {
        prepararAluno(nomeAluno);
    }

    @Quando("o sistema gerar a análise consolidada de desempenho do aluno")
    public void oSistemaGerarAAnaliseConsolidadaDeDesempenhoDoAluno() {
        try {
            analise = analisarDesempenhoAcademicoUseCase.executar(aluno.getId());
            excecao = null;
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            analise = null;
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema deve indicar risco acadêmico {string}")
    public void oSistemaDeveIndicarRiscoAcademico(String riscoEsperado) {
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(analise);
        assertEquals(Boolean.parseBoolean(riscoEsperado), analise.riscoAcademico());
    }

    @Então("o nível de risco deve ser {string}")
    public void oNivelDeRiscoDeveSer(String nivelEsperado) {
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(analise);
        assertEquals(nivelEsperado, analise.nivelRisco());
    }

    @Então("o sistema informa que o aluno está sem notas para análise")
    public void oSistemaInformaQueOAlunoEstaSemNotasParaAnalise() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Aluno sem notas para análise de desempenho", context.getMensagem());
    }

    private void prepararAluno(String nomeAluno) {
        context.resetMensagens();
        excecao = null;
        analise = null;
        aluno = criarAlunoUseCase.executar(nomeAluno, nomeAluno.toLowerCase().replace(" ", ".") + "@email.com");
    }
}
