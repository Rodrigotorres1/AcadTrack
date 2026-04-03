package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.bdd.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

public class SolicitarRetificacaoNotaSteps {

    private final TestContext context = new TestContext();
    private String alunoAtual;
    private String justificativaAtual;

    @Dado("que o aluno {string} possui uma nota lançada")
    public void queOAlunoPossuiUmaNotaLancada(String aluno) {
        alunoAtual = aluno;
        context.notasAluno.put(aluno, 7.0);
        context.mensagem = null;
        context.operacaoExecutada = false;
    }

    @Quando("ele solicita retificação informando uma justificativa")
    public void eleSolicitaRetificacaoInformandoUmaJustificativa() {
        justificativaAtual = "Houve erro na correção da questão discursiva";
        context.operacaoExecutada = true;
    }

    @Quando("ele solicita retificação sem justificativa")
    public void eleSolicitaRetificacaoSemJustificativa() {
        justificativaAtual = "";
        context.mensagem = "A justificativa é obrigatória";
        context.operacaoExecutada = false;
    }

    @Então("o sistema registra a solicitação de retificação")
    public void oSistemaRegistraASolicitacaoDeRetificacao() {
        assertTrue(context.operacaoExecutada);
        assertNotNull(justificativaAtual);
        assertFalse(justificativaAtual.isBlank());
    }

    @Então("o sistema informa que a justificativa é obrigatória")
    public void oSistemaInformaQueAJustificativaEObrigatoria() {
        assertFalse(context.operacaoExecutada);
        assertEquals("A justificativa é obrigatória", context.mensagem);
    }
}
