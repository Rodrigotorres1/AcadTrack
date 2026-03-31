package br.com.acadtrack.bdd.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TurmaSteps {

    private String turma;
    private String aluno;
    private boolean alunoJaVinculado;
    private String mensagem;

    @Dado("que o coordenador informou o nome da turma {string}")
    public void queOCoordenadorInformouONomeDaTurma(String turma) {
        this.turma = turma;
    }

    @Quando("o coordenador tentar cadastrar a turma")
    public void oCoordenadorTentarCadastrarATurma() {
        if (turma == null || turma.isBlank()) {
            mensagem = "O sistema informa que o nome da turma é obrigatório";
        } else {
            mensagem = "O sistema cadastra a turma com sucesso";
        }
    }

    @Então("o sistema cadastra a turma com sucesso")
    public void oSistemaCadastraATurmaComSucesso() {
        assertEquals("O sistema cadastra a turma com sucesso", mensagem);
    }

    @Então("o sistema informa que o nome da turma é obrigatório")
    public void oSistemaInformaQueONomeDaTurmaEObrigatorio() {
        assertEquals("O sistema informa que o nome da turma é obrigatório", mensagem);
    }

    @Dado("que existe a turma {string} cadastrada")
    public void queExisteATurmaCadastrada(String turma) {
        this.turma = turma;
    }

    @E("que o aluno {string} não está vinculado à turma {string}")
    public void queOAlunoNaoEstaVinculadoATurma(String aluno, String turma) {
        this.aluno = aluno;
        this.turma = turma;
        this.alunoJaVinculado = false;
    }

    @E("que o aluno {string} já está vinculado à turma {string}")
    public void queOAlunoJaEstaVinculadoATurma(String aluno, String turma) {
        this.aluno = aluno;
        this.turma = turma;
        this.alunoJaVinculado = true;
    }

    @Quando("o coordenador tentar vincular {word} {word} à turma {string}")
    public void oCoordenadorTentarVincularAlunoATurma(String nome, String sobrenome, String turma) {
        this.aluno = nome + " " + sobrenome;
        this.turma = turma;
        if (alunoJaVinculado) {
            mensagem = "O sistema informa que " + this.aluno + " já está vinculado à turma";
        } else {
            mensagem = "O sistema vincula " + this.aluno + " à turma com sucesso";
        }
    }

    @Quando("o coordenador tentar vincular {word} {word} à turma {string} novamente")
    public void oCoordenadorTentarVincularAlunoATurmaNovamente(String nome, String sobrenome, String turma) {
        oCoordenadorTentarVincularAlunoATurma(nome, sobrenome, turma);
    }

    @Então("o sistema vincula {word} {word} à turma com sucesso")
    public void oSistemaVinculaAlunoATurmaComSucesso(String nome, String sobrenome) {
        assertEquals("O sistema vincula " + nome + " " + sobrenome + " à turma com sucesso", mensagem);
    }

    @Então("o sistema informa que {word} {word} já está vinculado à turma")
    public void oSistemaInformaQueAlunoJaEstaVinculadoATurma(String nome, String sobrenome) {
        assertEquals("O sistema informa que " + nome + " " + sobrenome + " já está vinculado à turma", mensagem);
    }
}