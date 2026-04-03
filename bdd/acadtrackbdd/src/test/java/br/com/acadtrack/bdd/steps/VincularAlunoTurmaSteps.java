package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.bdd.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

public class VincularAlunoTurmaSteps {

    private final TestContext context = new TestContext();

    @Dado("que o aluno {string} não está vinculado a nenhuma turma")
    public void queOAlunoNaoEstaVinculadoANenhumaTurma(String aluno) {
        context.vinculosAlunoTurma.remove(aluno);
        context.mensagem = null;
    }

    @Dado("que o aluno {string} já está vinculado à turma {string}")
    public void queOAlunoJaEstaVinculadoATurma(String aluno, String turma) {
        context.vinculosAlunoTurma.put(aluno, turma);
        context.mensagem = null;
    }

    @Quando("o coordenador vincula o aluno {string} à turma {string}")
    public void oCoordenadorVinculaOAlunoATurma(String aluno, String turma) {
        if (context.vinculosAlunoTurma.containsKey(aluno)) {
            context.mensagem = "O aluno já está vinculado a uma turma";
            context.operacaoExecutada = false;
        } else {
            context.vinculosAlunoTurma.put(aluno, turma);
            context.operacaoExecutada = true;
        }
    }

    @Quando("o coordenador tenta vincular o aluno {string} à turma {string}")
    public void oCoordenadorTentaVincularOAlunoATurma(String aluno, String turma) {
        oCoordenadorVinculaOAlunoATurma(aluno, turma);
    }

    @Então("o sistema registra o vínculo do aluno {string} à turma {string}")
    public void oSistemaRegistraOVinculoDoAlunoATurma(String aluno, String turma) {
        assertTrue(context.operacaoExecutada);
        assertEquals(turma, context.vinculosAlunoTurma.get(aluno));
    }

    @Então("o sistema informa que o aluno já está vinculado a uma turma")
    public void oSistemaInformaQueOAlunoJaEstaVinculadoAUmaTurma() {
        assertFalse(context.operacaoExecutada);
        assertEquals("O aluno já está vinculado a uma turma", context.mensagem);
    }
}
