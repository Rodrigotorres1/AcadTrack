package g8.acadtrack.bdd.steps;

import g8.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import g8.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import g8.acadtrack.aplicacao.nota.LancarNotaUseCase;
import g8.acadtrack.aplicacao.relatorio.CriterioOrdenacaoRelatorio;
import g8.acadtrack.aplicacao.relatorio.GerarRelatorioDesempenhoAcademicoUseCase;
import g8.acadtrack.aplicacao.relatorio.RelatorioDesempenhoAcademicoItem;
import g8.acadtrack.aplicacao.relatorio.RelatorioDesempenhoAcademicoResultado;
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

public class RelatorioDesempenhoAcademicoSteps {

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final CriarSimuladoUseCase criarSimuladoUseCase;
    private final LancarNotaUseCase lancarNotaUseCase;
    private final GerarRelatorioDesempenhoAcademicoUseCase gerarRelatorioDesempenhoAcademicoUseCase;

    private Aluno alunoRiscoAlto;
    private Aluno alunoRiscoModerado;
    private Aluno alunoRiscoBaixo;
    private Aluno alunoSemNotas;
    private RelatorioDesempenhoAcademicoResultado relatorio;

    public RelatorioDesempenhoAcademicoSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            CriarSimuladoUseCase criarSimuladoUseCase,
            LancarNotaUseCase lancarNotaUseCase,
            GerarRelatorioDesempenhoAcademicoUseCase gerarRelatorioDesempenhoAcademicoUseCase
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.lancarNotaUseCase = lancarNotaUseCase;
        this.gerarRelatorioDesempenhoAcademicoUseCase = gerarRelatorioDesempenhoAcademicoUseCase;
    }

    @Dado("que existem alunos com desempenhos diferentes para relatorio academico")
    public void queExistemAlunosComDesempenhosDiferentesParaRelatorioAcademico() {
        context.resetMensagens();
        relatorio = null;
        String sufixo = String.valueOf(System.nanoTime());

        alunoRiscoAlto = criarAlunoUseCase.executar("Relatorio Alto " + sufixo, "relatorio.alto." + sufixo + "@email.com");
        alunoRiscoModerado = criarAlunoUseCase.executar("Relatorio Moderado " + sufixo, "relatorio.moderado." + sufixo + "@email.com");
        alunoRiscoBaixo = criarAlunoUseCase.executar("Relatorio Baixo " + sufixo, "relatorio.baixo." + sufixo + "@email.com");
        alunoSemNotas = criarAlunoUseCase.executar("Relatorio Sem Notas " + sufixo, "relatorio.semnotas." + sufixo + "@email.com");

        lancarNotasRiscoAlto(alunoRiscoAlto, sufixo);
        lancarNotasRiscoModerado(alunoRiscoModerado, sufixo);
        lancarNotasRiscoBaixo(alunoRiscoBaixo, sufixo);
    }

    @Quando("o sistema gerar o relatorio de desempenho academico com ordenacao {string}")
    public void oSistemaGerarORelatorioDeDesempenhoAcademicoComOrdenacao(String ordenacao) {
        relatorio = gerarRelatorioDesempenhoAcademicoUseCase.executar(CriterioOrdenacaoRelatorio.valueOf(ordenacao));
    }

    @Quando("o sistema gerar o relatorio de desempenho academico sem informar ordenacao")
    public void oSistemaGerarORelatorioDeDesempenhoAcademicoSemInformarOrdenacao() {
        relatorio = gerarRelatorioDesempenhoAcademicoUseCase.executar(null);
    }

    @Entao("os alunos do relatorio devem aparecer do maior para o menor risco academico")
    public void osAlunosDoRelatorioDevemAparecerDoMaiorParaOMenorRiscoAcademico() {
        List<RelatorioDesempenhoAcademicoItem> itens = itensDoCenario();

        assertEquals(alunoRiscoAlto.getId(), itens.get(0).alunoId());
        assertEquals(alunoRiscoModerado.getId(), itens.get(1).alunoId());
        assertEquals(alunoRiscoBaixo.getId(), itens.get(2).alunoId());
        assertEquals("ALTO", itens.get(0).nivelRisco());
        assertEquals("MODERADO", itens.get(1).nivelRisco());
        assertEquals("BAIXO", itens.get(2).nivelRisco());
    }

    @Entao("o relatorio deve informar a quantidade de alunos analisados")
    public void oRelatorioDeveInformarAQuantidadeDeAlunosAnalisados() {
        assertNotNull(relatorio);
        assertTrue(relatorio.totalAlunosAnalisados() >= 3);
    }

    @Entao("os alunos do relatorio devem aparecer da menor para a maior media geral")
    public void osAlunosDoRelatorioDevemAparecerDaMenorParaAMaiorMediaGeral() {
        List<RelatorioDesempenhoAcademicoItem> itens = itensDoCenario();

        assertEquals(alunoRiscoAlto.getId(), itens.get(0).alunoId());
        assertEquals(alunoRiscoModerado.getId(), itens.get(1).alunoId());
        assertEquals(alunoRiscoBaixo.getId(), itens.get(2).alunoId());
        assertTrue(itens.get(0).mediaGeral() < itens.get(1).mediaGeral());
        assertTrue(itens.get(1).mediaGeral() < itens.get(2).mediaGeral());
    }

    @Entao("os alunos do relatorio devem aparecer da melhor para a menor media geral")
    public void osAlunosDoRelatorioDevemAparecerDaMelhorParaAMenorMediaGeral() {
        List<RelatorioDesempenhoAcademicoItem> itens = itensDoCenario();

        assertEquals(alunoRiscoBaixo.getId(), itens.get(0).alunoId());
        assertEquals(alunoRiscoModerado.getId(), itens.get(1).alunoId());
        assertEquals(alunoRiscoAlto.getId(), itens.get(2).alunoId());
        assertTrue(itens.get(0).mediaGeral() > itens.get(1).mediaGeral());
        assertTrue(itens.get(1).mediaGeral() > itens.get(2).mediaGeral());
    }

    @Entao("o relatorio deve usar a ordenacao {string}")
    public void oRelatorioDeveUsarAOrdenacao(String ordenacaoEsperada) {
        assertNotNull(relatorio);
        assertEquals(CriterioOrdenacaoRelatorio.valueOf(ordenacaoEsperada), relatorio.ordenacao());
    }

    @Entao("o aluno sem notas nao deve aparecer no relatorio")
    public void oAlunoSemNotasNaoDeveAparecerNoRelatorio() {
        assertNotNull(relatorio);
        assertFalse(relatorio.alunos().stream().anyMatch(item -> alunoSemNotas.getId().equals(item.alunoId())));
    }

    private void lancarNotasRiscoAlto(Aluno aluno, String sufixo) {
        Disciplina matematica = criarDisciplinaUseCase.executar("Relatorio Matematica Alto " + sufixo);
        Disciplina portugues = criarDisciplinaUseCase.executar("Relatorio Portugues Alto " + sufixo);
        var simulado = criarSimuladoUseCase.executar(
                "Relatorio Simulado Alto " + sufixo,
                List.of(matematica.getId(), portugues.getId())
        );

        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), matematica.getId(), 3.0);
        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), portugues.getId(), 4.0);
    }

    private void lancarNotasRiscoModerado(Aluno aluno, String sufixo) {
        Disciplina matematica = criarDisciplinaUseCase.executar("Relatorio Matematica Moderado " + sufixo);
        Disciplina portugues = criarDisciplinaUseCase.executar("Relatorio Portugues Moderado " + sufixo);
        Disciplina historia = criarDisciplinaUseCase.executar("Relatorio Historia Moderado " + sufixo);

        var simuladoFraco = criarSimuladoUseCase.executar(
                "Relatorio Simulado Moderado Fraco " + sufixo,
                List.of(matematica.getId())
        );
        var simuladoBom = criarSimuladoUseCase.executar(
                "Relatorio Simulado Moderado Bom " + sufixo,
                List.of(portugues.getId(), historia.getId())
        );

        lancarNotaUseCase.executar(aluno.getId(), simuladoFraco.getId(), matematica.getId(), 4.0);
        lancarNotaUseCase.executar(aluno.getId(), simuladoBom.getId(), portugues.getId(), 8.0);
        lancarNotaUseCase.executar(aluno.getId(), simuladoBom.getId(), historia.getId(), 7.0);
    }

    private void lancarNotasRiscoBaixo(Aluno aluno, String sufixo) {
        Disciplina matematica = criarDisciplinaUseCase.executar("Relatorio Matematica Baixo " + sufixo);
        Disciplina portugues = criarDisciplinaUseCase.executar("Relatorio Portugues Baixo " + sufixo);
        var simulado = criarSimuladoUseCase.executar(
                "Relatorio Simulado Baixo " + sufixo,
                List.of(matematica.getId(), portugues.getId())
        );

        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), matematica.getId(), 8.0);
        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), portugues.getId(), 9.0);
    }

    private List<RelatorioDesempenhoAcademicoItem> itensDoCenario() {
        assertNotNull(relatorio);
        List<Long> idsDoCenario = List.of(
                alunoRiscoAlto.getId(),
                alunoRiscoModerado.getId(),
                alunoRiscoBaixo.getId()
        );

        List<RelatorioDesempenhoAcademicoItem> itens = relatorio.alunos().stream()
                .filter(item -> idsDoCenario.contains(item.alunoId()))
                .toList();

        assertEquals(3, itens.size());
        assertTrue(itens.stream().allMatch(item -> item.nomeAluno() != null && !item.nomeAluno().isBlank()));
        assertTrue(itens.stream().allMatch(item -> item.situacaoAcademica() != null));
        assertTrue(itens.stream().allMatch(item -> item.tipoPlanoEstudo() != null));
        return itens;
    }
}
