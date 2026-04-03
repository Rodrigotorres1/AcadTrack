package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.bdd.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CriarSimuladoSteps {

    private final TestContext context;
    private final String nomeSimulado = "Simulado Principal";

    public CriarSimuladoSteps(TestContext context) {
        this.context = context;
    }

    @Dado("que o coordenador deseja criar um simulado")
    public void queOCoordenadorDesejaCriarUmSimulado() {
        context.resetMensagens();
    }

    @Quando("ele informa as disciplinas {string} e {string}")
    public void eleInformaAsDisciplinasE(String d1, String d2) {
        context.getSimuladoDisciplinas().put(nomeSimulado, Arrays.asList(d1, d2));
        context.setOperacaoExecutada(true);
    }

    @Quando("ele não informa nenhuma disciplina")
    public void eleNaoInformaNenhumaDisciplina() {
        context.setMensagem("O simulado deve possuir pelo menos uma disciplina");
        context.setOperacaoExecutada(false);
    }

    @Então("o sistema cria o simulado com as disciplinas informadas")
    public void oSistemaCriaOSimuladoComAsDisciplinasInformadas() {
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(context.getSimuladoDisciplinas().get(nomeSimulado));
        assertEquals(2, context.getSimuladoDisciplinas().get(nomeSimulado).size());
    }

    @Então("o sistema informa que o simulado deve possuir pelo menos uma disciplina")
    public void oSistemaInformaQueOSimuladoDevePossuirPeloMenosUmaDisciplina() {
        assertFalse(context.isOperacaoExecutada());
        assertEquals("O simulado deve possuir pelo menos uma disciplina", context.getMensagem());
    }
}
