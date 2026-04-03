package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.bdd.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

public class VincularResponsavelSteps {

    private final TestContext context = new TestContext();
    private String alunoAtual;
    private String responsavelAtual;

    @Dado("que o aluno {string} não possui responsável vinculado")
    public void queOAlunoNaoPossuiResponsavelVinculado(String aluno) {
        alunoAtual = aluno;
        context.responsaveisAluno.remove(aluno);
        context.mensagem = null;
        context.operacaoExecutada = false;
    }

    @Quando("o coordenador vincula o responsável {string} ao aluno {string}")
    public void oCoordenadorVinculaOResponsavelAoAluno(String responsavel, String aluno) {
        responsavelAtual = responsavel;
        alunoAtual = aluno;
        context.responsaveisAluno.put(aluno, responsavel);
        context.operacaoExecutada = true;
    }

    @Quando("o coordenador tenta desvincular um responsável do aluno {string}")
    public void oCoordenadorTentaDesvincularUmResponsavelDoAluno(String aluno) {
        alunoAtual = aluno;

        if (!context.responsaveisAluno.containsKey(aluno)) {
            context.mensagem = "Não há responsável vinculado ao aluno";
            context.operacaoExecutada = false;
            return;
        }

        context.responsaveisAluno.remove(aluno);
        context.operacaoExecutada = true;
    }

    @Então("o sistema registra o responsável {string} para o aluno {string}")
    public void oSistemaRegistraOResponsavelParaOAluno(String responsavel, String aluno) {
        assertTrue(context.operacaoExecutada);
        assertEquals(responsavel, context.responsaveisAluno.get(aluno));
    }

    @Então("o sistema informa que não há responsável vinculado ao aluno")
    public void oSistemaInformaQueNaoHaResponsavelVinculadoAoAluno() {
        assertFalse(context.operacaoExecutada);
        assertEquals("Não há responsável vinculado ao aluno", context.mensagem);
    }
}
