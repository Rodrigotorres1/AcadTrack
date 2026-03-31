package br.com.acadtrack.bdd.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimuladoSteps {

    private String titulo;
    private String data;
    private String nomeSimulado;
    private String disciplina;
    private Integer peso;
    private String mensagem;

    @Dado("que o coordenador informou o título {string} e a data {string}")
    public void queOCoordenadorInformouOTituloEAData(String titulo, String data) {
        this.titulo = titulo;
        this.data = data;
    }

    @Quando("o coordenador tentar cadastrar o simulado")
    public void oCoordenadorTentarCadastrarOSimulado() {
        if (titulo == null || titulo.isBlank()) {
            mensagem = "O sistema informa que o título do simulado é obrigatório";
        } else {
            mensagem = "O sistema cadastra o simulado com sucesso";
        }
    }

    @Então("o sistema cadastra o simulado com sucesso")
    public void oSistemaCadastraOSimuladoComSucesso() {
        assertEquals("O sistema cadastra o simulado com sucesso", mensagem);
    }

    @Então("o sistema informa que o título do simulado é obrigatório")
    public void oSistemaInformaQueOTituloDoSimuladoEObrigatorio() {
        assertEquals("O sistema informa que o título do simulado é obrigatório", mensagem);
    }

    @Dado("que existe o simulado {string} cadastrado")
    public void queExisteOSimuladoCadastrado(String nomeSimulado) {
        this.nomeSimulado = nomeSimulado;
    }

    @E("que o coordenador informou a disciplina {string} com peso {int}")
    public void queOCoordenadorInformouADisciplinaComPeso(String disciplina, Integer peso) {
        this.disciplina = disciplina;
        this.peso = peso;
    }

    @Quando("o coordenador tentar adicionar a disciplina ao simulado")
    public void oCoordenadorTentarAdicionarADisciplinaAoSimulado() {
        if (nomeSimulado != null && peso != null && peso > 0) {
            mensagem = "O sistema adiciona a disciplina ao simulado com sucesso";
        } else {
            mensagem = "O sistema informa que o peso da disciplina deve ser maior que zero";
        }
    }

    @Então("o sistema adiciona a disciplina ao simulado com sucesso")
    public void oSistemaAdicionaADisciplinaAoSimuladoComSucesso() {
        assertEquals("O sistema adiciona a disciplina ao simulado com sucesso", mensagem);
    }

    @Então("o sistema informa que o peso da disciplina deve ser maior que zero")
    public void oSistemaInformaQueOPesoDaDisciplinaDeveSerMaiorQueZero() {
        assertEquals("O sistema informa que o peso da disciplina deve ser maior que zero", mensagem);
    }
}