package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.bdd.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

public class LancarNotaSteps {

    private final TestContext context;
    private String alunoAtual;

    public LancarNotaSteps(TestContext context) {
        this.context = context;
    }

    @Dado("que o aluno {string} realizou o simulado")
    public void queOAlunoRealizouOSimulado(String aluno) {
        this.alunoAtual = aluno;
        context.resetMensagens();
    }

    @Quando("o professor lança a nota {double} para o aluno {string}")
    public void oProfessorLancaANotaParaOAluno(Double nota, String aluno) {
        this.alunoAtual = aluno;

        if (nota == null || nota < 0 || nota > 10) {
            context.setMensagem("A nota é inválida");
            context.setOperacaoExecutada(false);
            return;
        }

        context.getNotasAluno().put(aluno, nota);
        context.setOperacaoExecutada(true);
    }

    @Então("o sistema registra a nota do aluno")
    public void oSistemaRegistraANotaDoAluno() {
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(context.getNotasAluno().get(alunoAtual));
    }

    @Então("o sistema informa que a nota é inválida")
    public void oSistemaInformaQueANotaEInvalida() {
        assertFalse(context.isOperacaoExecutada());
        assertEquals("A nota é inválida", context.getMensagem());
    }
}
