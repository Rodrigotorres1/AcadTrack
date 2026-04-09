package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import br.com.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import br.com.acadtrack.bdd.support.TestContext;
import br.com.acadtrack.dominioacademico.disciplina.Disciplina;
import br.com.acadtrack.dominioavaliacao.simulado.Simulado;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CriarSimuladoSteps {

    private final TestContext context;
    private final CriarSimuladoUseCase criarSimuladoUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;

    private final List<Long> disciplinasIds = new ArrayList<>();
    private Simulado simulado;
    private Exception excecao;

    public CriarSimuladoSteps(
            TestContext context,
            CriarSimuladoUseCase criarSimuladoUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase
    ) {
        this.context = context;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
    }

    @Dado("que o coordenador deseja criar um simulado")
    public void queOCoordenadorDesejaCriarUmSimulado() {
        context.resetMensagens();
        disciplinasIds.clear();
        simulado = null;
        excecao = null;
    }

    @Quando("ele informa as disciplinas {string} e {string}")
    public void eleInformaAsDisciplinasE(String d1, String d2) {
        try {
            Disciplina disciplina1 = criarDisciplinaUseCase.executar(d1);
            Disciplina disciplina2 = criarDisciplinaUseCase.executar(d2);

            disciplinasIds.add(disciplina1.getId());
            disciplinasIds.add(disciplina2.getId());

            simulado = criarSimuladoUseCase.executar("Simulado Principal", disciplinasIds);

            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("ele não informa nenhuma disciplina")
    public void eleNaoInformaNenhumaDisciplina() {
        try {
            simulado = criarSimuladoUseCase.executar("Simulado Principal", List.of());
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema cria o simulado com as disciplinas informadas")
    public void oSistemaCriaOSimuladoComAsDisciplinasInformadas() {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertNotNull(simulado);
    }

    @Então("o sistema informa que o simulado deve possuir pelo menos uma disciplina")
    public void oSistemaInformaQueOSimuladoDevePossuirPeloMenosUmaDisciplina() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("O simulado deve possuir pelo menos uma disciplina", context.getMensagem());
    }
}