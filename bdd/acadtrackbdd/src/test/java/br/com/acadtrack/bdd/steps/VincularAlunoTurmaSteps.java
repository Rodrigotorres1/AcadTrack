package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import br.com.acadtrack.aplicacao.turma.CriarTurmaUseCase;
import br.com.acadtrack.aplicacao.turma.VincularAlunoTurmaUseCase;
import br.com.acadtrack.bdd.support.TestContext;
import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.turma.Turma;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

public class VincularAlunoTurmaSteps {

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarTurmaUseCase criarTurmaUseCase;
    private final VincularAlunoTurmaUseCase vincularAlunoTurmaUseCase;

    private Aluno aluno;
    private Turma turma;
    private Exception excecao;

    public VincularAlunoTurmaSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarTurmaUseCase criarTurmaUseCase,
            VincularAlunoTurmaUseCase vincularAlunoTurmaUseCase
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarTurmaUseCase = criarTurmaUseCase;
        this.vincularAlunoTurmaUseCase = vincularAlunoTurmaUseCase;
    }

    @Dado("que o aluno {string} não está vinculado a nenhuma turma")
    public void queOAlunoNaoEstaVinculadoANenhumaTurma(String nomeAluno) {
        context.resetMensagens();
        excecao = null;

        aluno = criarAlunoUseCase.executar(nomeAluno, nomeAluno + "@email.com");
        turma = criarTurmaUseCase.executar("Turma A");
    }

    @Dado("que o aluno {string} já está vinculado à turma {string}")
    public void queOAlunoJaEstaVinculadoATurma(String nomeAluno, String nomeTurma) {
        context.resetMensagens();
        excecao = null;

        aluno = criarAlunoUseCase.executar(nomeAluno, nomeAluno + "@email.com");
        turma = criarTurmaUseCase.executar(nomeTurma);

        // primeira vinculação válida
        vincularAlunoTurmaUseCase.executar(aluno.getId(), turma.getId());
    }

    @Quando("o coordenador vincula o aluno {string} à turma {string}")
    public void oCoordenadorVinculaOAlunoATurma(String nomeAluno, String nomeTurma) {
        try {
            vincularAlunoTurmaUseCase.executar(aluno.getId(), turma.getId());
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("o coordenador tenta vincular o aluno {string} à turma {string}")
    public void oCoordenadorTentaVincularOAlunoATurma(String nomeAluno, String nomeTurma) {
        oCoordenadorVinculaOAlunoATurma(nomeAluno, nomeTurma);
    }

    @Então("o sistema registra o vínculo do aluno {string} à turma {string}")
    public void oSistemaRegistraOVinculoDoAlunoATurma(String nomeAluno, String nomeTurma) {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
    }

    @Então("o sistema informa que o aluno já está vinculado a uma turma")
    public void oSistemaInformaQueOAlunoJaEstaVinculadoAUmaTurma() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("O aluno já está vinculado a uma turma", context.getMensagem());
    }
}