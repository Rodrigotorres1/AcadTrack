package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.bdd.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GerarRankingSteps {

    private final TestContext context = new TestContext();

    @Dado("que existem alunos com notas lançadas no simulado")
    public void queExistemAlunosComNotasLancadasNoSimulado() {
        context.notasAluno.clear();
        context.notasAluno.put("João Silva", 8.5);
        context.notasAluno.put("Maria Souza", 9.0);
        context.notasAluno.put("Pedro Lima", 7.0);
        context.ranking.clear();
        context.mensagem = null;
        context.operacaoExecutada = false;
    }

    @Dado("que não existem notas lançadas no simulado")
    public void queNaoExistemNotasLancadasNoSimulado() {
        context.notasAluno.clear();
        context.ranking.clear();
        context.mensagem = null;
        context.operacaoExecutada = false;
    }

    @Quando("o sistema gera o ranking")
    public void oSistemaGeraORanking() {
        if (context.notasAluno.isEmpty()) {
            context.mensagem = "Não há dados suficientes para gerar o ranking";
            context.operacaoExecutada = false;
            return;
        }

        List<Map.Entry<String, Double>> lista = new ArrayList<>(context.notasAluno.entrySet());
        lista.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        context.ranking.clear();
        for (Map.Entry<String, Double> entry : lista) {
            context.ranking.add(entry.getKey());
        }

        context.operacaoExecutada = true;
    }

    @Quando("o sistema tenta gerar o ranking")
    public void oSistemaTentaGerarORanking() {
        oSistemaGeraORanking();
    }

    @Então("os alunos são ordenados do maior para o menor desempenho")
    public void osAlunosSaoOrdenadosDoMaiorParaOMenorDesempenho() {
        assertTrue(context.operacaoExecutada);
        assertEquals(3, context.ranking.size());
        assertEquals("Maria Souza", context.ranking.get(0));
        assertEquals("João Silva", context.ranking.get(1));
        assertEquals("Pedro Lima", context.ranking.get(2));
    }

    @Então("o sistema informa que não há dados suficientes para gerar o ranking")
    public void oSistemaInformaQueNaoHaDadosSuficientesParaGerarORanking() {
        assertFalse(context.operacaoExecutada);
        assertEquals("Não há dados suficientes para gerar o ranking", context.mensagem);
    }
}
