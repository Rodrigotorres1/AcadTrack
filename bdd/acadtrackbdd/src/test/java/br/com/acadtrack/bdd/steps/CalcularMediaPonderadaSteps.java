package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.bdd.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

public class CalcularMediaPonderadaSteps {

    private final TestContext context = new TestContext();

    @Dado("que o aluno possui notas nas disciplinas com pesos definidos")
    public void queOAlunoPossuiNotasNasDisciplinasComPesosDefinidos() {
        context.notasAluno.clear();
        context.pesosDisciplinas.clear();

        context.notasAluno.put("Matemática", 8.0);
        context.notasAluno.put("Português", 6.0);

        context.pesosDisciplinas.put("Matemática", 2.0);
        context.pesosDisciplinas.put("Português", 1.0);

        context.mediaCalculada = null;
        context.mensagem = null;
        context.operacaoExecutada = false;
    }

    @Dado("que existem disciplinas sem peso definido")
    public void queExistemDisciplinasSemPesoDefinido() {
        context.notasAluno.clear();
        context.pesosDisciplinas.clear();

        context.notasAluno.put("Matemática", 8.0);
        context.notasAluno.put("Português", 6.0);

        context.pesosDisciplinas.put("Matemática", 2.0);
        // Português sem peso

        context.mediaCalculada = null;
        context.mensagem = null;
        context.operacaoExecutada = false;
    }

    @Quando("o sistema calcula a média ponderada")
    public void oSistemaCalculaAMediaPonderada() {
        double somaNotasPesos = 0.0;
        double somaPesos = 0.0;

        for (String disciplina : context.notasAluno.keySet()) {
            Double peso = context.pesosDisciplinas.get(disciplina);
            if (peso == null) {
                context.mensagem = "Não é possível calcular a média";
                context.operacaoExecutada = false;
                return;
            }

            somaNotasPesos += context.notasAluno.get(disciplina) * peso;
            somaPesos += peso;
        }

        context.mediaCalculada = somaNotasPesos / somaPesos;
        context.operacaoExecutada = true;
    }

    @Quando("o sistema tenta calcular a média ponderada")
    public void oSistemaTentaCalcularAMediaPonderada() {
        oSistemaCalculaAMediaPonderada();
    }

    @Então("o sistema retorna a média correta do aluno")
    public void oSistemaRetornaAMediaCorretaDoAluno() {
        assertTrue(context.operacaoExecutada);
        assertNotNull(context.mediaCalculada);
        assertEquals(7.33, context.mediaCalculada, 0.01);
    }

    @Então("o sistema informa que não é possível calcular a média")
    public void oSistemaInformaQueNaoEPossivelCalcularAMedia() {
        assertFalse(context.operacaoExecutada);
        assertEquals("Não é possível calcular a média", context.mensagem);
    }
}
