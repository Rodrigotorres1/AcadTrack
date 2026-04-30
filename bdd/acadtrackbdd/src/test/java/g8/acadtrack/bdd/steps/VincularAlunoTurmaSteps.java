package g8.acadtrack.bdd.steps;

import g8.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import g8.acadtrack.aplicacao.turma.CriarTurmaUseCase;
import g8.acadtrack.aplicacao.turma.VincularAlunoTurmaUseCase;
import g8.acadtrack.bdd.support.TestContext;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.turma.Turma;
import io.cucumber.java.pt.*;

import static org.junit.jupiter.api.Assertions.*;

public class VincularAlunoTurmaSteps {

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarTurmaUseCase criarTurmaUseCase;
    private final VincularAlunoTurmaUseCase useCase;

    private Aluno aluno;
    private Turma turma;

    public VincularAlunoTurmaSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarTurmaUseCase criarTurmaUseCase,
            VincularAlunoTurmaUseCase useCase
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarTurmaUseCase = criarTurmaUseCase;
        this.useCase = useCase;
    }

    @Dado("que o aluno {string} não está vinculado a nenhuma turma")
    public void dadoAlunoSemTurma(String nome) {
        aluno = criarAlunoUseCase.executar(nome, nome + "@email.com");
        turma = criarTurmaUseCase.executar("Turma A");
    }

    @Dado("que o aluno {string} já está vinculado à turma {string}")
    public void dadoAlunoJaVinculado(String nome, String nomeTurma) {
        aluno = criarAlunoUseCase.executar(nome, nome + "@email.com");
        turma = criarTurmaUseCase.executar(nomeTurma);

        useCase.executar(aluno.getId(), turma.getId());
    }

    @Quando("o coordenador vincula o aluno {string} à turma {string}")
    public void quandoVincula(String nome, String turmaNome) {
        try {
            useCase.executar(aluno.getId(), turma.getId());
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema registra o vínculo do aluno {string} à turma {string}")
    public void entaoSucesso(String nome, String turmaNome) {
        assertTrue(context.isOperacaoExecutada());
    }

    @Então("o sistema informa que o aluno já está vinculado a uma turma")
    public void entaoErro() {
        assertFalse(context.isOperacaoExecutada());
        assertEquals("O aluno já está vinculado a uma turma", context.getMensagem());
    }
}