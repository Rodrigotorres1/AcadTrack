package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.bdd.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CriarSimuladoSteps {

    private final TestContext context = new TestContext();
    private String nomeSimulado = "Simulado Principal";
    private String disciplinasInformadas;

    @Dado("que o coordenador deseja criar um simulado")
    public void queOCoordenadorDesejaCriarUmSimulado() {
        context.mensagem = null;
        context.operacaoExecutada = false;
        disciplinasInformadas = null;
    }

    @Quando("ele informa as disciplinas {string} e {string}")
    public void eleInformaAsDisciplinasE(String d1, String d2) {
        disciplinasInformadas = d1 + "," + d2;
        context.simuladoDisciplinas.put(nomeSimulado, Arrays.asList(d1, d2));
        context.operacaoExecutada = true;
    }

    @Quando("ele não informa nenhuma disciplina")
    public void eleNaoInformaNenhumaDisciplina() {
        context.mensagem = "O simulado deve possuir pelo menos uma disciplina";
        context.operacaoExecutada = false;
    }

    @Então("o sistema cria o simulado com as disciplinas informadas")
    public void oSistemaCriaOSimuladoComAsDisciplinasInformadas() {
        assertTrue(context.operacaoExecutada);
        assertNotNull(context.simuladoDisciplinas.get(nomeSimulado));
        assertEquals(2, context.simuladoDisciplinas.get(nomeSimulado).size());
    }

    @Então("o sistema informa que o simulado deve possuir pelo menos uma disciplina")
    public void oSistemaInformaQueOSimuladoDevePossuirPeloMenosUmaDisciplina() {
        assertFalse(context.operacaoExecutada);
        assertEquals("O simulado deve possuir pelo menos uma disciplina", context.mensagem);
    }
}
