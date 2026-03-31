package br.com.acadtrack.bdd.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private String aluno;
    private boolean possuiNotas;
    private List<AlunoNota> ranking;
    private String mensagem;

    @Dado("que o aluno {string} possui notas registradas")
    public void queOAlunoPossuiNotasRegistradas(String aluno) {
        this.aluno = aluno;
        this.possuiNotas = true;
    }

    @Dado("que o aluno {string} não possui notas registradas")
    public void queOAlunoNaoPossuiNotasRegistradas(String aluno) {
        this.aluno = aluno;
        this.possuiNotas = false;
    }

    @Quando("{word} {word} tentar visualizar suas notas")
    public void alunoTentarVisualizarSuasNotas(String nome, String sobrenome) {
        this.aluno = nome + " " + sobrenome;
        if (possuiNotas) {
            mensagem = "O sistema exibe as notas de " + this.aluno + " com sucesso";
        } else {
            mensagem = "O sistema informa que " + this.aluno + " não possui notas registradas";
        }
    }

    @Então("o sistema exibe as notas de {word} {word} com sucesso")
    public void oSistemaExibeAsNotasDeAlunoComSucesso(String nome, String sobrenome) {
        assertEquals("O sistema exibe as notas de " + nome + " " + sobrenome + " com sucesso", mensagem);
    }

    @Então("o sistema informa que {word} {word} não possui notas registradas")
    public void oSistemaInformaQueAlunoNaoPossuiNotasRegistradas(String nome, String sobrenome) {
        assertEquals("O sistema informa que " + nome + " " + sobrenome + " não possui notas registradas", mensagem);
    }

    @Dado("que existem alunos com notas registradas no sistema")
    public void queExistemAlunosComNotasRegistradasNoSistema() {
        ranking = new ArrayList<>();
        ranking.add(new AlunoNota("Ana", 9.0));
        ranking.add(new AlunoNota("Carlos", 8.0));
        ranking.add(new AlunoNota("Bruno", 7.0));
    }

    @Dado("que não existem notas registradas no sistema")
    public void queNaoExistemNotasRegistradasNoSistema() {
        ranking = new ArrayList<>();
    }

    @Quando("o coordenador tentar visualizar o ranking")
    public void oCoordenadorTentarVisualizarORanking() {
        if (ranking != null && !ranking.isEmpty()) {
            ranking.sort(Comparator.comparingDouble((AlunoNota a) -> a.nota).reversed());
            mensagem = "O sistema exibe o ranking em ordem decrescente de desempenho";
        } else {
            mensagem = "O sistema informa que não é possível gerar o ranking sem notas";
        }
    }

    @Então("o sistema exibe o ranking em ordem decrescente de desempenho")
    public void oSistemaExibeORankingEmOrdemDecrescenteDeDesempenho() {
        assertEquals("O sistema exibe o ranking em ordem decrescente de desempenho", mensagem);
        assertTrue(ranking.get(0).nota >= ranking.get(1).nota);
    }

    @Então("o sistema informa que não é possível gerar o ranking sem notas")
    public void oSistemaInformaQueNaoEPossivelGerarORankingSemNotas() {
        assertEquals("O sistema informa que não é possível gerar o ranking sem notas", mensagem);
    }
}