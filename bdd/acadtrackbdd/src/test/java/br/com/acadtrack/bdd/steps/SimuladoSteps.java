package br.com.acadtrack.bdd.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimuladoSteps {

    private String titulo;
    private String data;
    private String disciplina;
    private Double peso;
    private boolean resultado;
    private boolean simuladoExiste;

    @Dado("que o coordenador informou um título válido para o simulado")
    public void tituloValido() {
        titulo = "Simulado AV1";
    }

    @Dado("que o coordenador não informou um título para o simulado")
    public void tituloInvalido() {
        titulo = "";
    }

    @E("informou uma data válida para o simulado")
    public void dataValida() {
        data = "2026-04-10";
    }

    @Quando("solicitar o cadastro do simulado")
    public void cadastrarSimulado() {
        resultado = titulo != null && !titulo.isBlank() && data != null && !data.isBlank();
    }

    @Então("o simulado deve ser cadastrado com sucesso")
    public void simuladoCadastrado() {
        assertTrue(resultado);
    }

    @Então("o sistema deve impedir o cadastro do simulado")
    public void simuladoNaoCadastrado() {
        assertFalse(resultado);
    }

    @Dado("que existe um simulado já cadastrado")
    public void existeSimulado() {
        simuladoExiste = true;
    }

    @E("o coordenador informou uma disciplina com peso válido")
    public void disciplinaPesoValido() {
        disciplina = "Matemática";
        peso = 2.0;
    }

    @E("o coordenador informou uma disciplina com peso inválido")
    public void disciplinaPesoInvalido() {
        disciplina = "Matemática";
        peso = -1.0;
    }

    @Quando("solicitar a adição da disciplina ao simulado")
    public void adicionarDisciplina() {
        resultado = simuladoExiste && disciplina != null && !disciplina.isBlank() && peso != null && peso > 0;
    }

    @Então("a disciplina deve ser adicionada com sucesso")
    public void disciplinaAdicionada() {
        assertTrue(resultado);
    }

    @Então("o sistema deve impedir a adição da disciplina")
    public void disciplinaNaoAdicionada() {
        assertFalse(resultado);
    }
}