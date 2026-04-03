package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.bdd.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

public class DefinirPesoDisciplinaSteps {

    private final TestContext context = new TestContext();
    private String disciplinaAtual;

    @Dado("que o simulado possui a disciplina {string}")
    public void queOSimuladoPossuiADisciplina(String disciplina) {
        disciplinaAtual = disciplina;
        context.mensagem = null;
        context.operacaoExecutada = false;
    }

    @Quando("o coordenador define o peso {double} para a disciplina {string}")
    public void oCoordenadorDefineOPesoParaADisciplina(Double peso, String disciplina) {
        if (peso <= 0) {
            context.mensagem = "O peso deve ser válido";
            context.operacaoExecutada = false;
            return;
        }

        context.pesosDisciplinas.put(disciplina, peso);
        context.operacaoExecutada = true;
    }

    @Então("o sistema registra o peso da disciplina corretamente")
    public void oSistemaRegistraOPesoDaDisciplinaCorretamente() {
        assertTrue(context.operacaoExecutada);
        assertNotNull(context.pesosDisciplinas.get(disciplinaAtual));
    }

    @Então("o sistema informa que o peso deve ser válido")
    public void oSistemaInformaQueOPesoDeveSerValido() {
        assertFalse(context.operacaoExecutada);
        assertEquals("O peso deve ser válido", context.mensagem);
    }
}
