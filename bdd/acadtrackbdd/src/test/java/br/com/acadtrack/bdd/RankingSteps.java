package br.com.acadtrack.bdd;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RankingSteps {

    static class AlunoNota {
        String nome;
        double nota;

        AlunoNota(String nome, double nota) {
            this.nome = nome;
            this.nota = nota;
        }
    }

    private List<AlunoNota> alunos;
    private List<AlunoNota> ranking;

    @Dado("que existem alunos com notas registradas")
    public void queExistemAlunosComNotasRegistradas() {
        alunos = new ArrayList<>();
        alunos.add(new AlunoNota("Ana", 9.0));
        alunos.add(new AlunoNota("Bruno", 7.5));
        alunos.add(new AlunoNota("Carlos", 8.0));
    }

    @Quando("o ranking for gerado")
    public void oRankingForGerado() {
        ranking = new ArrayList<>(alunos);
        ranking.sort(Comparator.comparingDouble((AlunoNota a) -> a.nota).reversed());
    }

    @Então("o aluno com maior nota deve ficar na primeira posição")
    public void oAlunoComMaiorNotaDeveFicarNaPrimeiraPosicao() {
        assertEquals("Ana", ranking.get(0).nome);
    }
}
