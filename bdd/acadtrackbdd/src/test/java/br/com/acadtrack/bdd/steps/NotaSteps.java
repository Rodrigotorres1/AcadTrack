package br.com.acadtrack.bdd.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotaSteps {

    private Double nota;
    private boolean alunoExiste;
    private boolean notaExiste;
    private boolean resultado;

    @Dado("que existe um aluno matriculado em uma turma")
    public void alunoMatriculado() {
        alunoExiste = true;
    }

    @E("o professor informou uma nota válida")
    public void notaValida() {
        nota = 8.5;
    }

    @E("o professor informou uma nota inválida")
    public void notaInvalida() {
        nota = 12.0;
    }

    @Quando("solicitar o lançamento da nota")
    public void lancarNota() {
        resultado = alunoExiste && nota != null && nota >= 0 && nota <= 10;
    }

    @Então("a nota deve ser registrada com sucesso")
    public void notaRegistrada() {
        assertTrue(resultado);
    }

    @Então("o sistema deve impedir o registro da nota")
    public void notaNaoRegistrada() {
        assertFalse(resultado);
    }

    @Dado("que existe uma nota já registrada para o aluno")
    public void existeNotaRegistrada() {
        notaExiste = true;
    }

    @Dado("que não existe nota registrada para o aluno")
    public void naoExisteNotaRegistrada() {
        notaExiste = false;
    }

    @E("o professor informou um novo valor válido para a nota")
    public void novoValorValido() {
        nota = 7.0;
    }

    @Quando("solicitar a correção da nota")
    public void corrigirNota() {
        resultado = notaExiste && nota != null && nota >= 0 && nota <= 10;
    }

    @Então("a nota deve ser atualizada com sucesso")
    public void notaAtualizada() {
        assertTrue(resultado);
    }

    @Então("o sistema deve impedir a correção da nota")
    public void notaNaoAtualizada() {
        assertFalse(resultado);
    }
}