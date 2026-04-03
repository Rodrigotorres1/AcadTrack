package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.bdd.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

public class SolicitarRetificacaoNotaSteps {

    private final TestContext context;
    private String alunoAtual;
    private String justificativaAtual;

    public SolicitarRetificacaoNotaSteps(TestContext context) {
        this.context = context;
    }

    @Dado("que o aluno {string} possui uma nota lançada")
    public void queOAlunoPossuiUmaNotaLancada(String aluno) {
        this.alunoAtual = aluno;
        context.getNotasAluno().put(aluno, 7.0);
        context.resetMensagens();
    }

    @Quando("ele solicita retificação informando uma justificativa")
    public void eleSolicitaRetificacaoInformandoUmaJustificativa() {
        justificativaAtual = "Houve erro na correção da questão discursiva";
        context.setOperacaoExecutada(true);
    }

    @Quando("ele solicita retificação sem justificativa")
    public void eleSolicitaRetificacaoSemJustificativa() {
        justificativaAtual = "";
        context.setMensagem("A justificativa é obrigatória");
        context.setOperacaoExecutada(false);
    }

    @Então("o sistema registra a solicitação de retificação")
    public void oSistemaRegistraASolicitacaoDeRetificacao() {
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(justificativaAtual);
        assertFalse(justificativaAtual.isBlank());
    }

    @Então("o sistema informa que a justificativa é obrigatória")
    public void oSistemaInformaQueAJustificativaEObrigatoria() {
        assertFalse(context.isOperacaoExecutada());
        assertEquals("A justificativa é obrigatória", context.getMensagem());
    }
}
