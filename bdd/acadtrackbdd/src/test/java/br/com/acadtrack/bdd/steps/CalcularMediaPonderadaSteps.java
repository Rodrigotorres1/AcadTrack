package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import br.com.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import br.com.acadtrack.aplicacao.nota.CalcularMediaPonderadaUseCase;
import br.com.acadtrack.aplicacao.nota.LancarNotaUseCase;
import br.com.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import br.com.acadtrack.bdd.support.TestContext;
import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.disciplina.Disciplina;
import br.com.acadtrack.dominioavaliacao.simulado.Simulado;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CalcularMediaPonderadaSteps {

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final CriarSimuladoUseCase criarSimuladoUseCase;
    private final LancarNotaUseCase lancarNotaUseCase;
    private final CalcularMediaPonderadaUseCase calcularMediaPonderadaUseCase;

    private Aluno aluno;
    private Disciplina disciplina1;
    private Disciplina disciplina2;
    private Simulado simulado;
    private Double mediaCalculada;
    private Exception excecao;

    public CalcularMediaPonderadaSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            CriarSimuladoUseCase criarSimuladoUseCase,
            LancarNotaUseCase lancarNotaUseCase,
            CalcularMediaPonderadaUseCase calcularMediaPonderadaUseCase
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.lancarNotaUseCase = lancarNotaUseCase;
        this.calcularMediaPonderadaUseCase = calcularMediaPonderadaUseCase;
    }

    @Dado("que o aluno possui notas nas disciplinas com pesos definidos")
    public void queOAlunoPossuiNotasNasDisciplinasComPesosDefinidos() {
        context.resetMensagens();
        mediaCalculada = null;
        excecao = null;

        aluno = criarAlunoUseCase.executar("João Silva", "joao@email.com");

        disciplina1 = criarDisciplinaUseCase.executar("Matemática");
        disciplina2 = criarDisciplinaUseCase.executar("Português");

        simulado = criarSimuladoUseCase.executar(
                "Simulado com pesos",
                List.of(disciplina1.getId(), disciplina2.getId())
        );

        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), disciplina1.getId(), 8.0);
        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), disciplina2.getId(), 6.0);
    }

    @Dado("que existem disciplinas sem peso definido")
    public void queExistemDisciplinasSemPesoDefinido() {
        context.resetMensagens();
        mediaCalculada = null;
        excecao = null;

        aluno = criarAlunoUseCase.executar("João Silva", "joao2@email.com");

        disciplina1 = criarDisciplinaUseCase.executar("História");
        disciplina2 = criarDisciplinaUseCase.executar("Geografia");

        simulado = criarSimuladoUseCase.executar(
                "Simulado incompleto",
                List.of(disciplina1.getId())
        );

        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), disciplina1.getId(), 8.0);
        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), disciplina2.getId(), 6.0);
    }

    @Quando("o sistema calcula a média ponderada")
    public void oSistemaCalculaAMediaPonderada() {
        try {
            mediaCalculada = calcularMediaPonderadaUseCase.executar(aluno.getId(), simulado.getId());
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
        assertNotNull(mediaCalculada);
        assertEquals(7.33, mediaCalculada, 0.01);
    }

    @Então("o sistema retorna média 0 para o aluno")
    public void oSistemaRetornaMediaZeroParaOAluno() {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertNotNull(mediaCalculada);
        assertEquals(0.0, mediaCalculada, 0.0);
    }
}