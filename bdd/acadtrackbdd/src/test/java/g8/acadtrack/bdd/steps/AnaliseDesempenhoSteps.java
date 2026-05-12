package g8.acadtrack.bdd.steps;

import g8.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import g8.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import g8.acadtrack.aplicacao.nota.AnaliseDesempenhoAcademicoResultado;
import g8.acadtrack.aplicacao.nota.AnalisarDesempenhoAcademicoUseCase;
import g8.acadtrack.aplicacao.nota.LancarNotaUseCase;
import g8.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import g8.acadtrack.bdd.support.TestContext;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class AnaliseDesempenhoSteps {

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final CriarSimuladoUseCase criarSimuladoUseCase;
    private final LancarNotaUseCase lancarNotaUseCase;
    private final AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase;
    private final MockMvc mockMvc;

    private Aluno aluno;
    private Exception excecao;
    private AnaliseDesempenhoAcademicoResultado analise;
    private MvcResult respostaApi;

    public AnaliseDesempenhoSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            CriarSimuladoUseCase criarSimuladoUseCase,
            LancarNotaUseCase lancarNotaUseCase,
            AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase,
            MockMvc mockMvc
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.lancarNotaUseCase = lancarNotaUseCase;
        this.analisarDesempenhoAcademicoUseCase = analisarDesempenhoAcademicoUseCase;
        this.mockMvc = mockMvc;
    }

    @Dado("que o aluno {string} possui histórico acadêmico consistente")
    public void queOAlunoPossuiHistoricoAcademicoConsistente(String nomeAluno) {
        prepararAluno(nomeAluno);

        Disciplina matematica = criarDisciplinaUseCase.executar("Matemática " + nomeAluno);
        Disciplina portugues = criarDisciplinaUseCase.executar("Português " + nomeAluno);
        Disciplina historia = criarDisciplinaUseCase.executar("História " + nomeAluno);

        Disciplina extra2 = criarDisciplinaUseCase.executar("Extra 2 " + nomeAluno);
        var simulado1 = criarSimuladoUseCase.executar("Simulado 1 " + nomeAluno, List.of(matematica.getId(), portugues.getId()));
        var simulado2 = criarSimuladoUseCase.executar("Simulado 2 " + nomeAluno, List.of(historia.getId(), extra2.getId()));

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

        Disciplina extraFraco = criarDisciplinaUseCase.executar("Extra fraco " + nomeAluno);
        var simuladoFraco = criarSimuladoUseCase.executar("Simulado fraco " + nomeAluno, List.of(matematica.getId(), extraFraco.getId()));
        var simuladoBom = criarSimuladoUseCase.executar("Simulado bom " + nomeAluno, List.of(portugues.getId(), historia.getId()));

        lancarNotaUseCase.executar(aluno.getId(), simuladoFraco.getId(), matematica.getId(), 4.0);
        lancarNotaUseCase.executar(aluno.getId(), simuladoBom.getId(), portugues.getId(), 8.0);
        lancarNotaUseCase.executar(aluno.getId(), simuladoBom.getId(), historia.getId(), 7.0);
    }

    @Dado("que o aluno {string} não possui notas lançadas")
    public void queOAlunoNaoPossuiNotasLancadas(String nomeAluno) {
        prepararAluno(nomeAluno);
    }

    @Dado("que o aluno {string} possui concorrentes no ranking academico")
    public void queOAlunoPossuiConcorrentesNoRankingAcademico(String nomeAluno) {
        prepararAluno(nomeAluno);

        Disciplina disciplina = criarDisciplinaUseCase.executar("Ranking disciplina " + nomeAluno);
        Disciplina extraRanking = criarDisciplinaUseCase.executar("Extra ranking " + nomeAluno);
        var simulado = criarSimuladoUseCase.executar("Simulado ranking " + nomeAluno, List.of(disciplina.getId(), extraRanking.getId()));

        Aluno primeiroLugar = criarAlunoUseCase.executar("Primeiro Ranking " + nomeAluno, "primeiro." + emailSeguro(nomeAluno));
        Aluno terceiroLugar = criarAlunoUseCase.executar("Terceiro Ranking " + nomeAluno, "terceiro." + emailSeguro(nomeAluno));

        lancarNotaUseCase.executar(primeiroLugar.getId(), simulado.getId(), disciplina.getId(), 9.5);
        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), disciplina.getId(), 8.0);
        lancarNotaUseCase.executar(terceiroLugar.getId(), simulado.getId(), disciplina.getId(), 6.5);
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

    @Quando("o cliente consulta a análise consolidada de desempenho do aluno pela API")
    public void oClienteConsultaAAnaliseConsolidadaDeDesempenhoDoAlunoPelaApi() throws Exception {
        respostaApi = mockMvc.perform(get("/alunos/{alunoId}/desempenho", aluno.getId()))
                .andReturn();
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
        assertEquals(nivelEsperado, analise.nivelRisco().name());
    }

    @Então("a análise deve informar posicao academica {string}")
    public void aAnaliseDeveInformarPosicaoAcademica(String posicaoEsperada) {
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(analise);
        assertEquals(Integer.valueOf(posicaoEsperada), analise.posicaoRanking());
    }

    @Então("a análise deve indicar aluno no Top 10 {string}")
    public void aAnaliseDeveIndicarAlunoNoTop10(String top10Esperado) {
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(analise);
        assertEquals(Boolean.parseBoolean(top10Esperado), analise.alunoNoTop10());
    }

    @Então("o sistema informa que o aluno está sem notas para análise")
    public void oSistemaInformaQueOAlunoEstaSemNotasParaAnalise() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertInstanceOf(RegraDeNegocioException.class, excecao);
        assertEquals("Aluno sem notas para análise de desempenho", context.getMensagem());
    }

    @Então("a API retorna erro 400 informando que o aluno está sem notas para análise")
    public void aApiRetornaErro400InformandoQueOAlunoEstaSemNotasParaAnalise() throws Exception {
        assertNotNull(respostaApi);
        assertEquals(400, respostaApi.getResponse().getStatus());
        assertTrue(respostaApi.getResponse().getContentAsString()
                .contains("\"message\":\"Aluno sem notas para análise de desempenho\""));
    }

    private void prepararAluno(String nomeAluno) {
        context.resetMensagens();
        excecao = null;
        analise = null;
        respostaApi = null;
        aluno = criarAlunoUseCase.executar(nomeAluno, emailSeguro(nomeAluno));
    }

    private String emailSeguro(String nomeAluno) {
        return nomeAluno.toLowerCase().replace(" ", ".") + "@email.com";
    }
}
