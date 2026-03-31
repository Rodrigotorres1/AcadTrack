package br.com.acadtrack.bdd.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotaSteps {

    private String aluno;
    private Double nota;
    private boolean notaExiste;
    private String mensagem;

    @Dado("que o aluno {string} está matriculado na turma {string}")
    public void queOAlunoEstaMatriculadoNaTurma(String aluno, String turma) {
        this.aluno = aluno;
    }

    @E("que o professor informou a nota {double} para {word} {word}")
    public void queOProfessorInformouANotaParaAluno(Double nota, String nome, String sobrenome) {
        this.nota = nota;
        this.aluno = nome + " " + sobrenome;
    }

    @Quando("o professor tentar lançar a nota de {word} {word}")
    public void oProfessorTentarLancarANotaDeAluno(String nome, String sobrenome) {
        this.aluno = nome + " " + sobrenome;
        if (nota != null && nota >= 0 && nota <= 10) {
            mensagem = "O sistema registra a nota com sucesso";
        } else {
            mensagem = "O sistema informa que a nota deve estar entre 0 e 10";
        }
    }

    @Então("o sistema registra a nota com sucesso")
    public void oSistemaRegistraANotaComSucesso() {
        assertEquals("O sistema registra a nota com sucesso", mensagem);
    }

    @Então("o sistema informa que a nota deve estar entre 0 e 10")
    public void oSistemaInformaQueANotaDeveEstarEntre0E10() {
        assertEquals("O sistema informa que a nota deve estar entre 0 e 10", mensagem);
    }

    @Dado("que {word} {word} já possui a nota {double} registrada")
    public void queAlunoJaPossuiANotaRegistrada(String nome, String sobrenome, Double notaAtual) {
        this.aluno = nome + " " + sobrenome;
        this.notaExiste = true;
    }

    @Dado("que {word} {word} não possui nota registrada")
    public void queAlunoNaoPossuiNotaRegistrada(String nome, String sobrenome) {
        this.aluno = nome + " " + sobrenome;
        this.notaExiste = false;
    }

    @E("que o professor informou a nova nota {double} para {word} {word}")
    public void queOProfessorInformouANovaNotaParaAluno(Double nota, String nome, String sobrenome) {
        this.nota = nota;
        this.aluno = nome + " " + sobrenome;
    }

    @Quando("o professor tentar corrigir a nota de {word} {word}")
    public void oProfessorTentarCorrigirANotaDeAluno(String nome, String sobrenome) {
        this.aluno = nome + " " + sobrenome;
        if (notaExiste) {
            mensagem = "O sistema atualiza a nota de " + this.aluno + " com sucesso";
        } else {
            mensagem = "O sistema informa que não existe nota cadastrada para " + this.aluno;
        }
    }

    @Então("o sistema atualiza a nota de {word} {word} com sucesso")
    public void oSistemaAtualizaANotaDeAlunoComSucesso(String nome, String sobrenome) {
        assertEquals("O sistema atualiza a nota de " + nome + " " + sobrenome + " com sucesso", mensagem);
    }

    @Então("o sistema informa que não existe nota cadastrada para {word} {word}")
    public void oSistemaInformaQueNaoExisteNotaCadastradaParaAluno(String nome, String sobrenome) {
        assertEquals("O sistema informa que não existe nota cadastrada para " + nome + " " + sobrenome, mensagem);
    }
}