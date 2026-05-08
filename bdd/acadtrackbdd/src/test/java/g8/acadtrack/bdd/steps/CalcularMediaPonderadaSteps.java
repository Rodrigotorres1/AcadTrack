package g8.acadtrack.bdd.steps;

import g8.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import g8.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import g8.acadtrack.aplicacao.nota.CalcularMediaPonderadaUseCase;
import g8.acadtrack.aplicacao.nota.LancarNotaUseCase;
import g8.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import g8.acadtrack.bdd.support.TestContext;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Dado("que o aluno possui notas nas disciplinas da composicao padrao")
    public void queOAlunoPossuiNotasNasDisciplinasDaComposicaoPadrao() {
        context.resetMensagens();
        mediaCalculada = null;
        excecao = null;

        aluno = criarAlunoUseCase.executar("Joao Silva", "joao.media.simulado@email.com");
        disciplina1 = criarDisciplinaUseCase.executar("Matematica");
        disciplina2 = criarDisciplinaUseCase.executar("Portugues");

        simulado = criarSimuladoUseCase.executar(
                "Simulado com composicao padrao",
                List.of(disciplina1.getId(), disciplina2.getId())
        );

        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), disciplina1.getId(), 8.0);
        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), disciplina2.getId(), 6.0);
    }

    @Quando("o sistema calcula a media por simulado")
    public void oSistemaCalculaAMediaPorSimulado() {
        try {
            mediaCalculada = calcularMediaPonderadaUseCase.executar(aluno.getId(), simulado.getId());
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Entao("o sistema retorna a media por simulado correta")
    public void oSistemaRetornaAMediaPorSimuladoCorreta() {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertNotNull(mediaCalculada);
        assertEquals(7.0, mediaCalculada, 0.01);
    }
}
