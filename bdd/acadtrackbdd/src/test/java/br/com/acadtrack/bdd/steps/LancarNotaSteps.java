package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.aplicacao.nota.LancarNotaUseCase;
import br.com.acadtrack.bdd.support.TestContext;
import br.com.acadtrack.dominioavaliacao.nota.Nota;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

public class LancarNotaSteps {

    private final TestContext context;
    private final LancarNotaUseCase lancarNotaUseCase;

    private Nota notaGerada;
    private Exception excecao;

    public LancarNotaSteps(TestContext context, LancarNotaUseCase lancarNotaUseCase) {
        this.context = context;
        this.lancarNotaUseCase = lancarNotaUseCase;
    }

    @Dado("que o aluno {string} realizou o simulado")
    public void queOAlunoRealizouOSimulado(String aluno) {
        context.resetMensagens();
        context.setAlunoAtual(aluno);

        notaGerada = null;
        excecao = null;
    }

    @Quando("o professor lança a nota {double} para o aluno {string}")
    public void oProfessorLancaANotaParaOAluno(Double nota, String aluno) {
        context.setAlunoAtual(aluno);

        try {
            notaGerada = lancarNotaUseCase.executar(1L, 1L, 1L, nota);
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem("A nota é inválida");
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema registra a nota do aluno")
    public void oSistemaRegistraANotaDoAluno() {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertNotNull(notaGerada);
        assertEquals(1L, notaGerada.getAlunoId());
        assertEquals(1L, notaGerada.getSimuladoId());
        assertEquals(1L, notaGerada.getDisciplinaId());
    }

    @Então("o sistema informa que a nota é inválida")
    public void oSistemaInformaQueANotaEInvalida() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("A nota é inválida", context.getMensagem());
    }
}