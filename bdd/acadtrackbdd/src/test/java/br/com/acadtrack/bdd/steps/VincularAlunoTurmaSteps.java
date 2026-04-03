package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.bdd.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

public class VincularAlunoTurmaSteps {

    private final TestContext context;

    public VincularAlunoTurmaSteps(TestContext context) {
        this.context = context;
    }

    @Dado("que o aluno {string} não está vinculado a nenhuma turma")
    public void queOAlunoNaoEstaVinculadoANenhumaTurma(String aluno) {
        context.getVinculosAlunoTurma().remove(aluno);
        context.resetMensagens();
    }

    @Dado("que o aluno {string} já está vinculado à turma {string}")
    public void queOAlunoJaEstaVinculadoATurma(String aluno, String turma) {
        context.getVinculosAlunoTurma().put(aluno, turma);
        context.resetMensagens();
    }

    @Quando("o coordenador vincula o aluno {string} à turma {string}")
    public void oCoordenadorVinculaOAlunoATurma(String aluno, String turma) {
        if (context.getVinculosAlunoTurma().containsKey(aluno)) {
            context.setMensagem("O aluno já está vinculado a uma turma");
            context.setOperacaoExecutada(false);
        } else {
            context.getVinculosAlunoTurma().put(aluno, turma);
            context.setOperacaoExecutada(true);
        }
    }

    @Quando("o coordenador tenta vincular o aluno {string} à turma {string}")
    public void oCoordenadorTentaVincularOAlunoATurma(String aluno, String turma) {
        oCoordenadorVinculaOAlunoATurma(aluno, turma);
    }

    @Então("o sistema registra o vínculo do aluno {string} à turma {string}")
    public void oSistemaRegistraOVinculoDoAlunoATurma(String aluno, String turma) {
        assertTrue(context.isOperacaoExecutada());
        assertEquals(turma, context.getVinculosAlunoTurma().get(aluno));
    }

    @Então("o sistema informa que o aluno já está vinculado a uma turma")
    public void oSistemaInformaQueOAlunoJaEstaVinculadoAUmaTurma() {
        assertFalse(context.isOperacaoExecutada());
        assertEquals("O aluno já está vinculado a uma turma", context.getMensagem());
    }
}
