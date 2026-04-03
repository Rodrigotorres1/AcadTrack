package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.bdd.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

public class CalcularMediaPonderadaSteps {

    private final TestContext context;

    public CalcularMediaPonderadaSteps(TestContext context) {
        this.context = context;
    }

    @Dado("que o aluno possui notas nas disciplinas com pesos definidos")
    public void queOAlunoPossuiNotasNasDisciplinasComPesosDefinidos() {
        context.getNotasAluno().clear();
        context.getPesosDisciplinas().clear();

        context.getNotasAluno().put("Matemática", 8.0);
        context.getNotasAluno().put("Português", 6.0);

        context.getPesosDisciplinas().put("Matemática", 2.0);
        context.getPesosDisciplinas().put("Português", 1.0);

        context.resetMensagens();
    }

    @Dado("que existem disciplinas sem peso definido")
    public void queExistemDisciplinasSemPesoDefinido() {
        context.getNotasAluno().clear();
        context.getPesosDisciplinas().clear();

        context.getNotasAluno().put("Matemática", 8.0);
        context.getNotasAluno().put("Português", 6.0);

        context.getPesosDisciplinas().put("Matemática", 2.0);
        // Português sem peso

        context.resetMensagens();
    }

    @Quando("o sistema calcula a média ponderada")
    public void oSistemaCalculaAMediaPonderada() {
        double somaNotasPesos = 0.0;
        double somaPesos = 0.0;

        for (String disciplina : context.getNotasAluno().keySet()) {
            Double peso = context.getPesosDisciplinas().get(disciplina);

            if (peso == null) {
                context.setMensagem("Não é possível calcular a média");
                context.setOperacaoExecutada(false);
                return;
            }

            somaNotasPesos += context.getNotasAluno().get(disciplina) * peso;
            somaPesos += peso;
        }

        context.setMediaCalculada(somaNotasPesos / somaPesos);
        context.setOperacaoExecutada(true);
    }

    @Quando("o sistema tenta calcular a média ponderada")
    public void oSistemaTentaCalcularAMediaPonderada() {
        oSistemaCalculaAMediaPonderada();
    }

    @Então("o sistema retorna a média correta do aluno")
    public void oSistemaRetornaAMediaCorretaDoAluno() {
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(context.getMediaCalculada());
        assertEquals(7.33, context.getMediaCalculada(), 0.01);
    }

    @Então("o sistema informa que não é possível calcular a média")
    public void oSistemaInformaQueNaoEPossivelCalcularAMedia() {
        assertFalse(context.isOperacaoExecutada());
        assertEquals("Não é possível calcular a média", context.getMensagem());
    }
}
