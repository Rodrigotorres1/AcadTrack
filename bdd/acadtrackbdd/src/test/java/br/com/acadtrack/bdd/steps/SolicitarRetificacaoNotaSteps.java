package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.aplicacao.retificacao.SolicitarRetificacaoNotaUseCase;
import br.com.acadtrack.bdd.support.TestContext;
import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

public class SolicitarRetificacaoNotaSteps {

    private final TestContext context;
    private final SolicitarRetificacaoNotaUseCase useCase;

    private SolicitacaoRetificacao solicitacaoGerada;
    private Exception excecao;

    public SolicitarRetificacaoNotaSteps(
            TestContext context,
            SolicitarRetificacaoNotaUseCase useCase
    ) {
        this.context = context;
        this.useCase = useCase;
    }

    @Dado("que o aluno {string} possui uma nota lançada")
    public void queOAlunoPossuiUmaNotaLancada(String aluno) {
        context.resetMensagens();
        context.setAlunoAtual(aluno);
        context.setNotaIdAtual(1L);
        context.getNotasAluno().put(aluno, 7.0);

        solicitacaoGerada = null;
        excecao = null;
    }

    @Quando("ele solicita retificação informando a justificativa {string}")
    public void eleSolicitaRetificacaoInformandoAJustificativa(String justificativa) {
        context.setJustificativaAtual(justificativa);

        try {
            solicitacaoGerada = useCase.executar(context.getNotaIdAtual(), justificativa);
            context.setStatusAtual(solicitacaoGerada.getStatus());
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("ele solicita retificação sem justificativa")
    public void eleSolicitaRetificacaoSemJustificativa() {
        context.setJustificativaAtual("");

        try {
            solicitacaoGerada = useCase.executar(context.getNotaIdAtual(), "");
            context.setStatusAtual(solicitacaoGerada.getStatus());
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema registra a solicitação de retificação com status {string}")
    public void oSistemaRegistraASolicitacaoDeRetificacaoComStatus(String statusEsperado) {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertNotNull(solicitacaoGerada);

        assertNotNull(context.getAlunoAtual());
        assertTrue(context.getNotasAluno().containsKey(context.getAlunoAtual()));
        assertNotNull(context.getNotaIdAtual());
        assertNotNull(context.getJustificativaAtual());
        assertFalse(context.getJustificativaAtual().isBlank());

        assertEquals(context.getNotaIdAtual(), solicitacaoGerada.getNotaId());
        assertEquals(statusEsperado, solicitacaoGerada.getStatus());
        assertEquals(statusEsperado, context.getStatusAtual());
    }

    @Então("o sistema informa que a justificativa é obrigatória")
    public void oSistemaInformaQueAJustificativaEObrigatoria() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Justificativa é obrigatória", context.getMensagem());
    }
}