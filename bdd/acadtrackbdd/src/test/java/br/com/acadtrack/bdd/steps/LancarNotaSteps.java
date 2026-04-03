package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.bdd.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

public class LancarNotaSteps {

    private final TestContext context = new TestContext();
    private String alunoAtual;

    @Dado("que o aluno {string} realizou o simulado")
    public void queOAlunoRealizouOSimulado(String aluno) {
        alunoAtual = aluno;
        context.mensagem = null;
        context.operacaoExecutada = false;
    }

    @Quando("o professor lança a nota {double} para o aluno {string}")
    public void oProfessorLancaANotaParaOAluno(Double nota, String aluno) {
        if (nota < 0 || nota > 10) {
            context.mensagem = "A nota é inválida";
            context.operacaoExecutada = false;
            return;
        }
        context.notasAluno.put(aluno, nota);
        context.operacaoExecutada = true;
    }

    @Então("o sistema registra a nota do aluno")
    public void oSistemaRegistraANotaDoAluno() {
        assertTrue(context.operacaoExecutada);
        assertNotNull(context.notasAluno.get(alunoAtual));
    }

    @Então("o sistema informa que a nota é inválida")
    public void oSistemaInformaQueANotaEInvalida() {
        assertFalse(context.operacaoExecutada);
        assertEquals("A nota é inválida", context.mensagem);
    }
}
