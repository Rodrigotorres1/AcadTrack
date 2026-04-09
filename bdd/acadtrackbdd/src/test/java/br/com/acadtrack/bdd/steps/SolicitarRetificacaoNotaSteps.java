package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import br.com.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import br.com.acadtrack.aplicacao.nota.LancarNotaUseCase;
import br.com.acadtrack.aplicacao.retificacao.SolicitarRetificacaoNotaUseCase;
import br.com.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import br.com.acadtrack.bdd.support.TestContext;
import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.disciplina.Disciplina;
import br.com.acadtrack.dominioavaliacao.nota.Nota;
import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import br.com.acadtrack.dominioavaliacao.simulado.Simulado;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SolicitarRetificacaoNotaSteps {

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final CriarSimuladoUseCase criarSimuladoUseCase;
    private final LancarNotaUseCase lancarNotaUseCase;
    private final SolicitarRetificacaoNotaUseCase solicitarRetificacaoNotaUseCase;

    private Aluno aluno;
    private Disciplina disciplina;
    private Simulado simulado;
    private Nota nota;
    private SolicitacaoRetificacao retificacao;
    private Exception excecao;

    public SolicitarRetificacaoNotaSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            CriarSimuladoUseCase criarSimuladoUseCase,
            LancarNotaUseCase lancarNotaUseCase,
            SolicitarRetificacaoNotaUseCase solicitarRetificacaoNotaUseCase
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.lancarNotaUseCase = lancarNotaUseCase;
        this.solicitarRetificacaoNotaUseCase = solicitarRetificacaoNotaUseCase;
    }

    @Dado("que o aluno {string} possui uma nota lançada")
    public void queOAlunoPossuiUmaNotaLancada(String nomeAluno) {
        context.resetMensagens();
        excecao = null;
        retificacao = null;

        aluno = criarAlunoUseCase.executar(nomeAluno, nomeAluno + "@email.com");
        disciplina = criarDisciplinaUseCase.executar("Matemática");

        simulado = criarSimuladoUseCase.executar(
                "Simulado de retificação",
                List.of(disciplina.getId())
        );

        nota = lancarNotaUseCase.executar(
                aluno.getId(),
                simulado.getId(),
                disciplina.getId(),
                7.0
        );
    }

    @Quando("ele solicita retificação informando a justificativa {string}")
    public void eleSolicitaRetificacaoInformandoAJustificativa(String justificativa) {
        try {
            retificacao = solicitarRetificacaoNotaUseCase.executar(nota.getId(), justificativa);
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("ele solicita retificação sem justificativa")
    public void eleSolicitaRetificacaoSemJustificativa() {
        eleSolicitaRetificacaoInformandoAJustificativa("");
    }

    @Entao("o sistema registra a solicitação de retificação com status {string}")
    public void oSistemaRegistraASolicitacaoDeRetificacaoComStatus(String statusEsperado) {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertEquals(statusEsperado, retificacao.getStatus());
    }

    @Entao("o sistema informa que a justificativa é obrigatória")
    public void oSistemaInformaQueAJustificativaEObrigatoria() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Justificativa é obrigatória", context.getMensagem());
    }
}