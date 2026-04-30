package g8.acadtrack.bdd.steps;

import g8.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import g8.acadtrack.aplicacao.responsavel.ConsultarNotasAlunoPorResponsavelUseCase;
import g8.acadtrack.aplicacao.responsavel.CriarResponsavelUseCase;
import g8.acadtrack.aplicacao.responsavel.VincularResponsavelUseCase;
import g8.acadtrack.aplicacao.responsavel.DesvincularResponsavelUseCase;
import g8.acadtrack.bdd.support.TestContext;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominiousuarios.responsavel.Responsavel;
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
    private final ConsultarNotasAlunoPorResponsavelUseCase consultarNotasAlunoPorResponsavelUseCase;

    private Aluno aluno;
    private Responsavel responsavel;
    private Exception excecao;

    public VincularResponsavelSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarResponsavelUseCase criarResponsavelUseCase,
            VincularResponsavelUseCase vincularResponsavelUseCase,
            DesvincularResponsavelUseCase desvincularResponsavelUseCase,
            ConsultarNotasAlunoPorResponsavelUseCase consultarNotasAlunoPorResponsavelUseCase
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarResponsavelUseCase = criarResponsavelUseCase;
        this.vincularResponsavelUseCase = vincularResponsavelUseCase;
        this.desvincularResponsavelUseCase = desvincularResponsavelUseCase;
        this.consultarNotasAlunoPorResponsavelUseCase = consultarNotasAlunoPorResponsavelUseCase;
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
            vincularResponsavelUseCase.executar(
                    aluno.getId(),
                    responsavel.getId(),
                    true,
                    true,
                    true
            );
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

    @Dado("que o aluno {string} já possui vínculo ativo com responsável")
    public void queOAlunoJaPossuiVinculoAtivoComResponsavel(String nomeAluno) {
        context.resetMensagens();
        excecao = null;

        aluno = criarAlunoUseCase.executar(nomeAluno, nomeAluno + "@email.com");
        responsavel = criarResponsavelUseCase.executar("Responsável " + nomeAluno, nomeAluno + "@resp.com");
        vincularResponsavelUseCase.executar(aluno.getId(), responsavel.getId(), true, true, true);
    }

    @Quando("o coordenador tenta vincular novamente o mesmo responsável ao aluno {string}")
    public void oCoordenadorTentaVincularNovamenteOMesmoResponsavelAoAluno(String nomeAluno) {
        try {
            vincularResponsavelUseCase.executar(aluno.getId(), responsavel.getId(), true, true, true);
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema informa que já existe vínculo ativo entre aluno e responsável")
    public void oSistemaInformaQueJaExisteVinculoAtivoEntreAlunoEResponsavel() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Já existe vínculo ativo entre aluno e responsável", context.getMensagem());
    }

    @Dado("que existe aluno e responsável sem vínculo ativo")
    public void queExisteAlunoEResponsavelSemVinculoAtivo() {
        context.resetMensagens();
        excecao = null;
        aluno = criarAlunoUseCase.executar("Aluno Sem Vinculo", "sem.vinculo@email.com");
        responsavel = criarResponsavelUseCase.executar("Responsável Sem Vinculo", "sem.vinculo@resp.com");
    }

    @Quando("o responsável tenta consultar notas do aluno sem vínculo ativo")
    public void oResponsavelTentaConsultarNotasDoAlunoSemVinculoAtivo() {
        try {
            consultarNotasAlunoPorResponsavelUseCase.executar(responsavel.getId(), aluno.getId());
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema bloqueia o acesso por vínculo inativo")
    public void oSistemaBloqueiaOAcessoPorVinculoInativo() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Responsável sem vínculo ativo com o aluno", context.getMensagem());
    }

    @Dado("que o aluno possui vínculo ativo com responsável sem permissão para notas")
    public void queOAlunoPossuiVinculoAtivoComResponsavelSemPermissaoParaNotas() {
        context.resetMensagens();
        excecao = null;
        aluno = criarAlunoUseCase.executar("Aluno Sem Permissao", "sem.permissao@email.com");
        responsavel = criarResponsavelUseCase.executar("Responsável Sem Permissao", "sem.permissao@resp.com");
        vincularResponsavelUseCase.executar(aluno.getId(), responsavel.getId(), false, true, true);
    }

    @Quando("o responsável tenta consultar notas do aluno sem permissão")
    public void oResponsavelTentaConsultarNotasDoAlunoSemPermissao() {
        try {
            consultarNotasAlunoPorResponsavelUseCase.executar(responsavel.getId(), aluno.getId());
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema bloqueia o acesso por permissão insuficiente")
    public void oSistemaBloqueiaOAcessoPorPermissaoInsuficiente() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Responsável não possui permissão para acessar este recurso", context.getMensagem());
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
        assertEquals("Não há vínculo ativo de responsável para o aluno", context.getMensagem());
    }
}