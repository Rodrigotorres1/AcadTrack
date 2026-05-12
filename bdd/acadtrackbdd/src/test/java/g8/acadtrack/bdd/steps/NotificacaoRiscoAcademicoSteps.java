package g8.acadtrack.bdd.steps;

import g8.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import g8.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import g8.acadtrack.aplicacao.nota.LancarNotaUseCase;
import g8.acadtrack.aplicacao.notificacao.ListarNotificacoesResponsavelUseCase;
import g8.acadtrack.aplicacao.responsavel.CriarResponsavelUseCase;
import g8.acadtrack.aplicacao.responsavel.VincularResponsavelUseCase;
import g8.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import g8.acadtrack.bdd.support.TestContext;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominiousuarios.notificacao.NotificacaoResponsavel;
import g8.acadtrack.dominiousuarios.responsavel.Responsavel;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotificacaoRiscoAcademicoSteps {

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarResponsavelUseCase criarResponsavelUseCase;
    private final VincularResponsavelUseCase vincularResponsavelUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final CriarSimuladoUseCase criarSimuladoUseCase;
    private final LancarNotaUseCase lancarNotaUseCase;
    private final ListarNotificacoesResponsavelUseCase listarNotificacoesResponsavelUseCase;

    private Aluno aluno;
    private Responsavel responsavel;
    private Exception excecao;
    private List<NotificacaoResponsavel> notificacoes;
    private NotificacaoResponsavel notificacaoEncontrada;

    public NotificacaoRiscoAcademicoSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarResponsavelUseCase criarResponsavelUseCase,
            VincularResponsavelUseCase vincularResponsavelUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            CriarSimuladoUseCase criarSimuladoUseCase,
            LancarNotaUseCase lancarNotaUseCase,
            ListarNotificacoesResponsavelUseCase listarNotificacoesResponsavelUseCase
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarResponsavelUseCase = criarResponsavelUseCase;
        this.vincularResponsavelUseCase = vincularResponsavelUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.lancarNotaUseCase = lancarNotaUseCase;
        this.listarNotificacoesResponsavelUseCase = listarNotificacoesResponsavelUseCase;
    }

    @Dado("que o aluno {string} possui responsavel vinculado para notificacao")
    public void queOAlunoPossuiResponsavelVinculadoParaNotificacao(String nomeAluno) {
        prepararAluno(nomeAluno);
        responsavel = criarResponsavelUseCase.executar(
                "Responsavel Notificacao " + nomeAluno,
                emailSeguro(nomeAluno) + ".notificacao.resp@email.com"
        );
        vincularResponsavelUseCase.executar(aluno.getId(), responsavel.getId(), true, true, true);
    }

    @Dado("que o aluno {string} nao possui responsavel vinculado para notificacao")
    public void queOAlunoNaoPossuiResponsavelVinculadoParaNotificacao(String nomeAluno) {
        prepararAluno(nomeAluno);
    }

    @Quando("o professor lanca notas que geram risco academico ALTO")
    public void oProfessorLancaNotasQueGeramRiscoAcademicoAlto() {
        try {
            Disciplina matematica = criarDisciplinaUseCase.executar("Matematica notificacao alto " + aluno.getNome());
            Disciplina portugues = criarDisciplinaUseCase.executar("Portugues notificacao alto " + aluno.getNome());
            var simulado = criarSimuladoUseCase.executar(
                    "Simulado notificacao alto " + aluno.getNome(),
                    List.of(matematica.getId(), portugues.getId())
            );

            lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), matematica.getId(), 3.0);
            lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), portugues.getId(), 4.0);
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("o professor lanca notas que geram risco academico MODERADO")
    public void oProfessorLancaNotasQueGeramRiscoAcademicoModerado() {
        try {
            Disciplina matematica = criarDisciplinaUseCase.executar("Matematica notificacao moderado " + aluno.getNome());
            Disciplina portugues = criarDisciplinaUseCase.executar("Portugues notificacao moderado " + aluno.getNome());
            Disciplina historia = criarDisciplinaUseCase.executar("Historia notificacao moderado " + aluno.getNome());

            var simuladoFraco = criarSimuladoUseCase.executar(
                    "Simulado notificacao moderado fraco " + aluno.getNome(),
                    List.of(matematica.getId(), historia.getId())
            );
            var simuladoBom = criarSimuladoUseCase.executar(
                    "Simulado notificacao moderado bom " + aluno.getNome(),
                    List.of(portugues.getId(), historia.getId())
            );

            lancarNotaUseCase.executar(aluno.getId(), simuladoFraco.getId(), matematica.getId(), 4.0);
            lancarNotaUseCase.executar(aluno.getId(), simuladoBom.getId(), portugues.getId(), 8.0);
            lancarNotaUseCase.executar(aluno.getId(), simuladoBom.getId(), historia.getId(), 7.0);
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("o professor lanca notas que mantem risco academico BAIXO")
    public void oProfessorLancaNotasQueMantemRiscoAcademicoBaixo() {
        try {
            Disciplina matematica = criarDisciplinaUseCase.executar("Matematica notificacao baixo " + aluno.getNome());
            Disciplina portugues = criarDisciplinaUseCase.executar("Portugues notificacao baixo " + aluno.getNome());
            var simulado = criarSimuladoUseCase.executar(
                    "Simulado notificacao baixo " + aluno.getNome(),
                    List.of(matematica.getId(), portugues.getId())
            );

            lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), matematica.getId(), 8.0);
            lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), portugues.getId(), 9.0);
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("o responsavel consulta suas notificacoes")
    public void oResponsavelConsultaSuasNotificacoes() {
        notificacoes = listarNotificacoesResponsavelUseCase.executar(responsavel.getId());
    }

    @Entao("o responsavel deve receber notificacao com nivel de risco {string} e prioridade {string}")
    public void oResponsavelDeveReceberNotificacaoComNivelDeRiscoEPrioridade(String nivelRisco, String prioridade) {
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(responsavel);

        notificacoes = listarNotificacoesResponsavelUseCase.executar(responsavel.getId());
        notificacaoEncontrada = notificacoes.stream()
                .filter(notificacao -> nivelRisco.equals(notificacao.getNivelRisco().name()))
                .filter(notificacao -> prioridade.equals(notificacao.getPrioridade().name()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Notificacao esperada nao encontrada"));

        assertEquals(aluno.getId(), notificacaoEncontrada.getAlunoId());
        assertEquals(responsavel.getId(), notificacaoEncontrada.getResponsavelId());
        assertNotNull(notificacaoEncontrada.getMensagem());
        assertNotNull(notificacaoEncontrada.getDataCriacao());
    }

    @Entao("a notificacao deve estar com status {string}")
    public void aNotificacaoDeveEstarComStatus(String status) {
        assertNotNull(notificacaoEncontrada);
        assertEquals(status, notificacaoEncontrada.getStatus().name());
    }

    @Entao("o responsavel nao deve receber notificacao de risco academico")
    public void oResponsavelNaoDeveReceberNotificacaoDeRiscoAcademico() {
        assertTrue(context.isOperacaoExecutada());
        notificacoes = listarNotificacoesResponsavelUseCase.executar(responsavel.getId());
        assertTrue(notificacoes.isEmpty());
    }

    @Entao("o lancamento de notas em risco academico deve ser concluido sem falha")
    public void oLancamentoDeNotasEmRiscoAcademicoDeveSerConcluidoSemFalha() {
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(aluno);
        assertNull(excecao);
    }

    @Entao("o sistema deve listar as notificacoes do responsavel")
    public void oSistemaDeveListarAsNotificacoesDoResponsavel() {
        assertNotNull(notificacoes);
        assertFalse(notificacoes.isEmpty());
        assertTrue(notificacoes.stream().allMatch(notificacao -> responsavel.getId().equals(notificacao.getResponsavelId())));
    }

    private void prepararAluno(String nomeAluno) {
        context.resetMensagens();
        excecao = null;
        notificacoes = List.of();
        notificacaoEncontrada = null;
        responsavel = null;
        aluno = criarAlunoUseCase.executar(nomeAluno, emailSeguro(nomeAluno) + ".notificacao.aluno@email.com");
    }

    private String emailSeguro(String nome) {
        return nome.toLowerCase().replace(" ", ".");
    }
}
