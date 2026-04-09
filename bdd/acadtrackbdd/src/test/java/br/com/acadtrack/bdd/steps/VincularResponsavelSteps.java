package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import br.com.acadtrack.aplicacao.responsavel.CriarResponsavelUseCase;
import br.com.acadtrack.aplicacao.responsavel.VincularResponsavelUseCase;
import br.com.acadtrack.aplicacao.responsavel.DesvincularResponsavelUseCase;
import br.com.acadtrack.bdd.support.TestContext;
import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominiousuarios.responsavel.Responsavel;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

public class VincularResponsavelSteps {

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarResponsavelUseCase criarResponsavelUseCase;
    private final VincularResponsavelUseCase vincularResponsavelUseCase;
    private final DesvincularResponsavelUseCase desvincularResponsavelUseCase;

    private Aluno aluno;
    private Responsavel responsavel;
    private Exception excecao;

    public VincularResponsavelSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarResponsavelUseCase criarResponsavelUseCase,
            VincularResponsavelUseCase vincularResponsavelUseCase,
            DesvincularResponsavelUseCase desvincularResponsavelUseCase
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarResponsavelUseCase = criarResponsavelUseCase;
        this.vincularResponsavelUseCase = vincularResponsavelUseCase;
        this.desvincularResponsavelUseCase = desvincularResponsavelUseCase;
    }

    @Dado("que o aluno {string} não possui responsável vinculado")
    public void queOAlunoNaoPossuiResponsavelVinculado(String nomeAluno) {
        context.resetMensagens();
        excecao = null;

        aluno = criarAlunoUseCase.executar(nomeAluno, nomeAluno + "@email.com");
        responsavel = criarResponsavelUseCase.executar("Responsável " + nomeAluno, nomeAluno + "@resp.com");
    }

    @Quando("o coordenador vincula o responsável {string} ao aluno {string}")
    public void oCoordenadorVinculaOResponsavelAoAluno(String nomeResponsavel, String nomeAluno) {
        try {
            vincularResponsavelUseCase.executar(aluno.getId(), responsavel.getId());
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("o coordenador tenta desvincular um responsável do aluno {string}")
    public void oCoordenadorTentaDesvincularUmResponsavelDoAluno(String nomeAluno) {
        try {
            desvincularResponsavelUseCase.executar(aluno.getId());
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema registra o responsável {string} para o aluno {string}")
    public void oSistemaRegistraOResponsavelParaOAluno(String nomeResponsavel, String nomeAluno) {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
    }

    @Então("o sistema informa que não há responsável vinculado ao aluno")
    public void oSistemaInformaQueNaoHaResponsavelVinculadoAoAluno() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Não há responsável vinculado ao aluno", context.getMensagem());
    }
}