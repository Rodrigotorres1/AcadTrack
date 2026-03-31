
package br.com.acadtrack.bdd.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RankingSteps {

    static class AlunoNota {
        String nome;
        double nota;

        AlunoNota(String nome, double nota) {
            this.nome = nome;
            this.nota = nota;
        }
    }

    private boolean possuiNotas;
    private List<AlunoNota> ranking;
    private boolean resultado;

    @Dado("que o aluno possui notas registradas")
    public void alunoPossuiNotas() {
        possuiNotas = true;
    }

    @Dado("que o aluno não possui notas registradas")
    public void alunoNaoPossuiNotas() {
        possuiNotas = false;
    }

    @Quando("solicitar a visualização das notas")
    public void visualizarNotas() {
        resultado = possuiNotas;
    }

    @Então("as notas do aluno devem ser exibidas com sucesso")
    public void notasExibidas() {
        assertTrue(resultado);
    }

    @Então("o sistema deve informar que não existem notas registradas")
    public void notasNaoExibidas() {
        assertFalse(resultado);
    }

    @Dado("que existem alunos com notas registradas")
    public void existemAlunosComNotas() {
        ranking = new ArrayList<>();
        ranking.add(new AlunoNota("Ana", 9.0));
        ranking.add(new AlunoNota("Bruno", 7.0));
        ranking.add(new AlunoNota("Carlos", 8.0));
    }

    @Dado("que não existem notas registradas para os alunos")
    public void naoExistemNotas() {
        ranking = new ArrayList<>();
    }

    @Quando("solicitar a visualização do ranking")
    public void visualizarRanking() {
        resultado = ranking != null && !ranking.isEmpty();
        if (resultado) {
            ranking.sort(Comparator.comparingDouble((AlunoNota a) -> a.nota).reversed());
        }
    }

    @Então("o ranking deve ser exibido em ordem decrescente de desempenho")
    public void rankingExibido() {
        assertTrue(resultado);
        assertTrue(ranking.get(0).nota >= ranking.get(1).nota);
    }

    @Então("o sistema deve informar que não é possível gerar o ranking")
    public void rankingNaoExibido() {
        assertFalse(resultado);
    }
}