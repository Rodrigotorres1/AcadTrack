package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.aplicacao.nota.CalcularMediaPonderadaUseCase;
import br.com.acadtrack.bdd.support.TestContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

public class CalcularMediaPonderadaSteps {

    private final TestContext context;
    private final CalcularMediaPonderadaUseCase calcularMediaPonderadaUseCase;

    private Double mediaCalculada;
    private Exception excecao;

    public CalcularMediaPonderadaSteps(
            TestContext context,
            CalcularMediaPonderadaUseCase calcularMediaPonderadaUseCase
    ) {
        this.context = context;
        this.calcularMediaPonderadaUseCase = calcularMediaPonderadaUseCase;
    }

    @Dado("que o aluno possui notas nas disciplinas com pesos definidos")
    public void queOAlunoPossuiNotasNasDisciplinasComPesosDefinidos() {
        context.resetMensagens();
        mediaCalculada = null;
        excecao = null;
    }

    @Dado("que existem disciplinas sem peso definido")
    public void queExistemDisciplinasSemPesoDefinido() {
        context.resetMensagens();
        mediaCalculada = null;
        excecao = null;
    }

    @Quando("o sistema calcula a média ponderada")
    public void oSistemaCalculaAMediaPonderada() {
        try {
            mediaCalculada = calcularMediaPonderadaUseCase.executar(1L, 1L);
            context.setMediaCalculada(mediaCalculada);
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("o sistema tenta calcular a média ponderada")
    public void oSistemaTentaCalcularAMediaPonderada() {
        oSistemaCalculaAMediaPonderada();
    }

    @Então("o sistema retorna a média correta do aluno")
    public void oSistemaRetornaAMediaCorretaDoAluno() {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertNotNull(context.getMediaCalculada());
        assertEquals(7.33, context.getMediaCalculada(), 0.01);
    }

    @Então("Então o sistema retorna média 0 para o aluno")
    public void oSistemaInformaQueNaoEPossivelCalcularAMedia() {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertNotNull(context.getMediaCalculada());
        assertEquals(0.0, context.getMediaCalculada(), 0.0);
    }
}