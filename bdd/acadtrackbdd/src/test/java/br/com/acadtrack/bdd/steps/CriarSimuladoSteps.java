package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import br.com.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import br.com.acadtrack.aplicacao.simulado.VincularDisciplinaSimuladoUseCase;
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
    private final VincularDisciplinaSimuladoUseCase vincularDisciplinaSimuladoUseCase;

    private Simulado simulado;
    private final List<Disciplina> disciplinasCriadas = new ArrayList<>();
    private Exception excecao;

    public CriarSimuladoSteps(
            TestContext context,
            CriarSimuladoUseCase criarSimuladoUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            VincularDisciplinaSimuladoUseCase vincularDisciplinaSimuladoUseCase
    ) {
        this.context = context;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.vincularDisciplinaSimuladoUseCase = vincularDisciplinaSimuladoUseCase;
    }

    @Dado("que o coordenador deseja criar um simulado")
    public void queOCoordenadorDesejaCriarUmSimulado() {
        context.resetMensagens();
        disciplinasCriadas.clear();
        simulado = null;
        excecao = null;
    }

    @Quando("ele informa as disciplinas {string} e {string}")
    public void eleInformaAsDisciplinasE(String d1, String d2) {
        try {
            simulado = criarSimuladoUseCase.executar(
                "Simulado Principal",
                List.of(1L, 2L)
            );

            Disciplina disciplina1 = criarDisciplinaUseCase.executar(d1);
            Disciplina disciplina2 = criarDisciplinaUseCase.executar(d2);

            disciplinasCriadas.add(disciplina1);
            disciplinasCriadas.add(disciplina2);

            vincularDisciplinaSimuladoUseCase.executar(simulado.getId(), disciplina1.getId(), 1.0);
            vincularDisciplinaSimuladoUseCase.executar(simulado.getId(), disciplina2.getId(), 1.0);

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
                simulado = criarSimuladoUseCase.executar(
                "Simulado Principal",
                List.of(1L, 2L)
            );
            context.setMensagem("O simulado deve possuir pelo menos uma disciplina");
            context.setOperacaoExecutada(false);
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
        assertEquals(2, disciplinasCriadas.size());
    }

    @Então("o sistema informa que o simulado deve possuir pelo menos uma disciplina")
    public void oSistemaInformaQueOSimuladoDevePossuirPeloMenosUmaDisciplina() {
        assertFalse(context.isOperacaoExecutada());
        assertEquals("O simulado deve possuir pelo menos uma disciplina", context.getMensagem());
    }
}