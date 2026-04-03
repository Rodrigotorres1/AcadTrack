package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.bdd.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

public class DefinirPesoDisciplinaSteps {

    private final TestContext context;
    private String disciplinaAtual;

    public DefinirPesoDisciplinaSteps(TestContext context) {
        this.context = context;
    }

    @Dado("que o simulado possui a disciplina {string}")
    public void queOSimuladoPossuiADisciplina(String disciplina) {
        this.disciplinaAtual = disciplina;
        context.resetMensagens();
    }

    @Quando("o coordenador define o peso {double} para a disciplina {string}")
    public void oCoordenadorDefineOPesoParaADisciplina(Double peso, String disciplina) {
        this.disciplinaAtual = disciplina;

        if (peso == null || peso <= 0) {
            context.setMensagem("O peso deve ser válido");
            context.setOperacaoExecutada(false);
            return;
        }

        context.getPesosDisciplinas().put(disciplina, peso);
        context.setOperacaoExecutada(true);
    }

    @Então("o sistema registra o peso da disciplina corretamente")
    public void oSistemaRegistraOPesoDaDisciplinaCorretamente() {
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(context.getPesosDisciplinas().get(disciplinaAtual));
    }

    @Então("o sistema informa que o peso deve ser válido")
    public void oSistemaInformaQueOPesoDeveSerValido() {
        assertFalse(context.isOperacaoExecutada());
        assertEquals("O peso deve ser válido", context.getMensagem());
    }
}
