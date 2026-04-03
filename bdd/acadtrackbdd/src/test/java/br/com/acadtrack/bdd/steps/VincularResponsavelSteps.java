package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.bdd.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

public class VincularResponsavelSteps {

    private final TestContext context;

    public VincularResponsavelSteps(TestContext context) {
        this.context = context;
    }

    @Dado("que o aluno {string} não possui responsável vinculado")
    public void queOAlunoNaoPossuiResponsavelVinculado(String aluno) {
        context.getResponsaveisAluno().remove(aluno);
        context.resetMensagens();
    }

    @Quando("o coordenador vincula o responsável {string} ao aluno {string}")
    public void oCoordenadorVinculaOResponsavelAoAluno(String responsavel, String aluno) {
        context.getResponsaveisAluno().put(aluno, responsavel);
        context.setOperacaoExecutada(true);
    }

    @Quando("o coordenador tenta desvincular um responsável do aluno {string}")
    public void oCoordenadorTentaDesvincularUmResponsavelDoAluno(String aluno) {
        if (!context.getResponsaveisAluno().containsKey(aluno)) {
            context.setMensagem("Não há responsável vinculado ao aluno");
            context.setOperacaoExecutada(false);
            return;
        }

        context.getResponsaveisAluno().remove(aluno);
        context.setOperacaoExecutada(true);
    }

    @Então("o sistema registra o responsável {string} para o aluno {string}")
    public void oSistemaRegistraOResponsavelParaOAluno(String responsavel, String aluno) {
        assertTrue(context.isOperacaoExecutada());
        assertEquals(responsavel, context.getResponsaveisAluno().get(aluno));
    }

    @Então("o sistema informa que não há responsável vinculado ao aluno")
    public void oSistemaInformaQueNaoHaResponsavelVinculadoAoAluno() {
        assertFalse(context.isOperacaoExecutada());
        assertEquals("Não há responsável vinculado ao aluno", context.getMensagem());
    }
}
