package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.bdd.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GerarRankingSteps {

    private final TestContext context;

    public GerarRankingSteps(TestContext context) {
        this.context = context;
    }

    @Dado("que existem alunos com notas lançadas no simulado")
    public void queExistemAlunosComNotasLancadasNoSimulado() {
        context.getNotasAluno().clear();
        context.getNotasAluno().put("João Silva", 8.5);
        context.getNotasAluno().put("Maria Souza", 9.0);
        context.getNotasAluno().put("Pedro Lima", 7.0);
        context.getRanking().clear();
        context.resetMensagens();
    }

    @Dado("que não existem notas lançadas no simulado")
    public void queNaoExistemNotasLancadasNoSimulado() {
        context.getNotasAluno().clear();
        context.getRanking().clear();
        context.resetMensagens();
    }

    @Quando("o sistema gera o ranking")
    public void oSistemaGeraORanking() {
        if (context.getNotasAluno().isEmpty()) {
            context.setMensagem("Não há dados suficientes para gerar o ranking");
            context.setOperacaoExecutada(false);
            return;
        }

        List<Map.Entry<String, Double>> lista = new ArrayList<>(context.getNotasAluno().entrySet());
        lista.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        context.getRanking().clear();
        for (Map.Entry<String, Double> entry : lista) {
            context.getRanking().add(entry.getKey());
        }

        context.setOperacaoExecutada(true);
    }

    @Quando("o sistema tenta gerar o ranking")
    public void oSistemaTentaGerarORanking() {
        oSistemaGeraORanking();
    }

    @Então("os alunos são ordenados do maior para o menor desempenho")
    public void osAlunosSaoOrdenadosDoMaiorParaOMenorDesempenho() {
        assertTrue(context.isOperacaoExecutada());
        assertEquals(3, context.getRanking().size());
        assertEquals("Maria Souza", context.getRanking().get(0));
        assertEquals("João Silva", context.getRanking().get(1));
        assertEquals("Pedro Lima", context.getRanking().get(2));
    }

    @Então("o sistema informa que não há dados suficientes para gerar o ranking")
    public void oSistemaInformaQueNaoHaDadosSuficientesParaGerarORanking() {
        assertFalse(context.isOperacaoExecutada());
        assertEquals("Não há dados suficientes para gerar o ranking", context.getMensagem());
    }
}
