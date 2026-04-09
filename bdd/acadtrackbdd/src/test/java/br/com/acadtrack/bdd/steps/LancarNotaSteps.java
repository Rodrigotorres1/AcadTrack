package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import br.com.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import br.com.acadtrack.aplicacao.nota.LancarNotaUseCase;
import br.com.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import br.com.acadtrack.bdd.support.TestContext;
import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.disciplina.Disciplina;
import br.com.acadtrack.dominioavaliacao.nota.Nota;
import br.com.acadtrack.dominioavaliacao.simulado.Simulado;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LancarNotaSteps {

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final CriarSimuladoUseCase criarSimuladoUseCase;
    private final LancarNotaUseCase lancarNotaUseCase;

    private Aluno aluno;
    private Disciplina disciplina;
    private Simulado simulado;
    private Nota notaLancada;
    private Exception excecao;

    public LancarNotaSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            CriarSimuladoUseCase criarSimuladoUseCase,
            LancarNotaUseCase lancarNotaUseCase
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.lancarNotaUseCase = lancarNotaUseCase;
    }

    @Dado("que o aluno {string} realizou o simulado")
    public void queOAlunoRealizouOSimulado(String nomeAluno) {
        context.resetMensagens();
        excecao = null;
        notaLancada = null;

        aluno = criarAlunoUseCase.executar(nomeAluno, nomeAluno + "@email.com");
        disciplina = criarDisciplinaUseCase.executar("Matemática");

        simulado = criarSimuladoUseCase.executar(
                "Simulado de lançamento de nota",
                List.of(disciplina.getId())
        );
    }

    @Quando("o professor lança a nota {double} para o aluno {string}")
    public void oProfessorLancaANotaParaOAluno(Double valorNota, String nomeAluno) {
        try {
            notaLancada = lancarNotaUseCase.executar(
                    aluno.getId(),
                    simulado.getId(),
                    disciplina.getId(),
                    valorNota
            );
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema registra a nota do aluno")
    public void oSistemaRegistraANotaDoAluno() {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertNotNull(notaLancada);
        assertEquals(aluno.getId(), notaLancada.getAlunoId());
        assertEquals(simulado.getId(), notaLancada.getSimuladoId());
        assertEquals(disciplina.getId(), notaLancada.getDisciplinaId());
    }

    @Então("o sistema informa que a nota é inválida")
    public void oSistemaInformaQueANotaEInvalida() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("A nota é inválida", context.getMensagem());
    }
}