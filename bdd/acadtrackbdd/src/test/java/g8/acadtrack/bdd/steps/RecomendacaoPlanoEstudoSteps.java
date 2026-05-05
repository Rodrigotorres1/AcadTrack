package g8.acadtrack.bdd.steps;

import g8.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import g8.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import g8.acadtrack.aplicacao.nota.LancarNotaUseCase;
import g8.acadtrack.aplicacao.planoestudo.PlanoEstudoRecomendadoResultado;
import g8.acadtrack.aplicacao.planoestudo.RecomendarPlanoEstudoUseCase;
import g8.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import g8.acadtrack.bdd.support.TestContext;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RecomendacaoPlanoEstudoSteps {

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final CriarSimuladoUseCase criarSimuladoUseCase;
    private final LancarNotaUseCase lancarNotaUseCase;
    private final RecomendarPlanoEstudoUseCase recomendarPlanoEstudoUseCase;

    private Aluno aluno;
    private Exception excecao;
    private PlanoEstudoRecomendadoResultado recomendacao;

    public RecomendacaoPlanoEstudoSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            CriarSimuladoUseCase criarSimuladoUseCase,
            LancarNotaUseCase lancarNotaUseCase,
            RecomendarPlanoEstudoUseCase recomendarPlanoEstudoUseCase
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.lancarNotaUseCase = lancarNotaUseCase;
        this.recomendarPlanoEstudoUseCase = recomendarPlanoEstudoUseCase;
    }

    @Dado("que o aluno {string} possui desempenho com risco alto para plano de estudo")
    public void queOAlunoPossuiDesempenhoComRiscoAltoParaPlanoDeEstudo(String nomeAluno) {
        prepararAluno(nomeAluno);

        Disciplina matematica = criarDisciplinaUseCase.executar("Matematica plano alto " + nomeAluno);
        Disciplina portugues = criarDisciplinaUseCase.executar("Portugues plano alto " + nomeAluno);
        var simulado = criarSimuladoUseCase.executar(
                "Simulado plano alto " + nomeAluno,
                List.of(matematica.getId(), portugues.getId())
        );

        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), matematica.getId(), 3.0);
        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), portugues.getId(), 4.0);
    }

    @Dado("que o aluno {string} possui desempenho com risco moderado para plano de estudo")
    public void queOAlunoPossuiDesempenhoComRiscoModeradoParaPlanoDeEstudo(String nomeAluno) {
        prepararAluno(nomeAluno);

        Disciplina matematica = criarDisciplinaUseCase.executar("Matematica plano moderado " + nomeAluno);
        Disciplina portugues = criarDisciplinaUseCase.executar("Portugues plano moderado " + nomeAluno);
        Disciplina historia = criarDisciplinaUseCase.executar("Historia plano moderado " + nomeAluno);

        var simuladoFraco = criarSimuladoUseCase.executar(
                "Simulado plano moderado fraco " + nomeAluno,
                List.of(matematica.getId())
        );
        var simuladoBom = criarSimuladoUseCase.executar(
                "Simulado plano moderado bom " + nomeAluno,
                List.of(portugues.getId(), historia.getId())
        );

        lancarNotaUseCase.executar(aluno.getId(), simuladoFraco.getId(), matematica.getId(), 4.0);
        lancarNotaUseCase.executar(aluno.getId(), simuladoBom.getId(), portugues.getId(), 8.0);
        lancarNotaUseCase.executar(aluno.getId(), simuladoBom.getId(), historia.getId(), 7.0);
    }

    @Dado("que o aluno {string} possui desempenho avancado para plano de estudo")
    public void queOAlunoPossuiDesempenhoAvancadoParaPlanoDeEstudo(String nomeAluno) {
        prepararAluno(nomeAluno);

        Disciplina matematica = criarDisciplinaUseCase.executar("Matematica plano avancado " + nomeAluno);
        Disciplina portugues = criarDisciplinaUseCase.executar("Portugues plano avancado " + nomeAluno);
        var simulado = criarSimuladoUseCase.executar(
                "Simulado plano avancado " + nomeAluno,
                List.of(matematica.getId(), portugues.getId())
        );

        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), matematica.getId(), 8.0);
        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), portugues.getId(), 9.0);
    }

    @Dado("que o aluno {string} possui desempenho de manutencao para plano de estudo")
    public void queOAlunoPossuiDesempenhoDeManutencaoParaPlanoDeEstudo(String nomeAluno) {
        prepararAluno(nomeAluno);

        Disciplina matematica = criarDisciplinaUseCase.executar("Matematica plano manutencao " + nomeAluno);
        Disciplina portugues = criarDisciplinaUseCase.executar("Portugues plano manutencao " + nomeAluno);
        var simulado = criarSimuladoUseCase.executar(
                "Simulado plano manutencao " + nomeAluno,
                List.of(matematica.getId(), portugues.getId())
        );

        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), matematica.getId(), 7.0);
        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), portugues.getId(), 7.5);
    }

    @Dado("que o aluno {string} nao possui notas para plano de estudo")
    public void queOAlunoNaoPossuiNotasParaPlanoDeEstudo(String nomeAluno) {
        prepararAluno(nomeAluno);
    }

    @Quando("o sistema recomendar o plano de estudo do aluno")
    public void oSistemaRecomendarOPlanoDeEstudoDoAluno() {
        try {
            recomendacao = recomendarPlanoEstudoUseCase.executar(aluno.getId());
            excecao = null;
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            recomendacao = null;
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Entao("o sistema deve recomendar o tipo de plano {string}")
    public void oSistemaDeveRecomendarOTipoDePlano(String tipoPlanoEsperado) {
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(recomendacao);
        assertEquals(tipoPlanoEsperado, recomendacao.tipoPlano().name());
        assertNotNull(recomendacao.descricao());
        assertTrue(recomendacao.cargaHorariaSemanalSugerida() > 0);
        assertFalse(recomendacao.orientacoes().isEmpty());
    }

    @Entao("a recomendacao deve conter nivel de risco {string}")
    public void aRecomendacaoDeveConterNivelDeRisco(String nivelRiscoEsperado) {
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(recomendacao);
        assertEquals(nivelRiscoEsperado, recomendacao.nivelRisco());
        assertEquals(aluno.getId(), recomendacao.alunoId());
    }

    @Entao("a recomendacao deve conter media geral maior ou igual a {double}")
    public void aRecomendacaoDeveConterMediaGeralMaiorOuIgualA(Double mediaMinima) {
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(recomendacao);
        assertTrue(recomendacao.mediaGeral() >= mediaMinima);
    }

    @Entao("a recomendacao deve conter media geral menor que {double}")
    public void aRecomendacaoDeveConterMediaGeralMenorQue(Double mediaMaxima) {
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(recomendacao);
        assertTrue(recomendacao.mediaGeral() < mediaMaxima);
    }

    @Entao("o sistema informa que o aluno esta sem notas para recomendacao de plano")
    public void oSistemaInformaQueOAlunoEstaSemNotasParaRecomendacaoDePlano() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Aluno sem notas para analise de desempenho", context.getMensagem());
    }

    private void prepararAluno(String nomeAluno) {
        context.resetMensagens();
        excecao = null;
        recomendacao = null;
        aluno = criarAlunoUseCase.executar(nomeAluno, nomeAluno.toLowerCase().replace(" ", ".") + ".plano@email.com");
    }
}
