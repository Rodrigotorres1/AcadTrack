package br.com.acadtrack.bdd.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TurmaSteps {

    private String nomeTurma;
    private boolean resultado;
    private boolean turmaExiste;
    private boolean alunoJaVinculado;

    @Dado("que o coordenador informou um nome válido para a turma")
    public void nomeValidoTurma() {
        nomeTurma = "3A";
    }

    @Dado("que o coordenador não informou um nome para a turma")
    public void nomeInvalidoTurma() {
        nomeTurma = "";
    }

    @Quando("solicitar o cadastro da turma")
    public void cadastrarTurma() {
        resultado = nomeTurma != null && !nomeTurma.isBlank();
    }

    @Então("a turma deve ser cadastrada com sucesso")
    public void turmaCadastrada() {
        assertTrue(resultado);
    }

    @Então("o sistema deve impedir o cadastro da turma")
    public void turmaNaoCadastrada() {
        assertFalse(resultado);
    }

    @Dado("que existe uma turma cadastrada")
    public void turmaExiste() {
        turmaExiste = true;
    }

    @E("existe um aluno ainda não vinculado à turma")
    public void alunoNaoVinculado() {
        alunoJaVinculado = false;
    }

    @E("existe um aluno já vinculado à turma")
    public void alunoJaVinculado() {
        alunoJaVinculado = true;
    }

    @Quando("o coordenador solicitar a vinculação do aluno à turma")
    public void vincularAluno() {
        resultado = turmaExiste && !alunoJaVinculado;
    }

    @Então("o aluno deve ser vinculado com sucesso")
    public void alunoVinculado() {
        assertTrue(resultado);
    }

    @Então("o sistema deve impedir a vinculação duplicada")
    public void alunoNaoVinculadoDuplicado() {
        assertFalse(resultado);
    }
}