package g8.acadtrack.bdd.steps;

import g8.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import g8.acadtrack.aplicacao.responsavel.CriarResponsavelUseCase;
import g8.acadtrack.aplicacao.responsavel.ExcluirResponsavelUseCase;
import g8.acadtrack.aplicacao.responsavel.VincularResponsavelUseCase;
import g8.acadtrack.bdd.support.TestContext;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominiousuarios.responsavel.Responsavel;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExcluirResponsavelSteps {

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarResponsavelUseCase criarResponsavelUseCase;
    private final VincularResponsavelUseCase vincularResponsavelUseCase;
    private final ExcluirResponsavelUseCase excluirResponsavelUseCase;
    private final AlunoRepository alunoRepository;

    private Aluno aluno;
    private Responsavel responsavel;
    private Long responsavelIdInexistente;
    private Exception excecao;

    public ExcluirResponsavelSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarResponsavelUseCase criarResponsavelUseCase,
            VincularResponsavelUseCase vincularResponsavelUseCase,
            ExcluirResponsavelUseCase excluirResponsavelUseCase,
            AlunoRepository alunoRepository
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarResponsavelUseCase = criarResponsavelUseCase;
        this.vincularResponsavelUseCase = vincularResponsavelUseCase;
        this.excluirResponsavelUseCase = excluirResponsavelUseCase;
        this.alunoRepository = alunoRepository;
    }

    @Dado("que existe um responsavel {string} cadastrado sem aluno vinculado")
    public void queExisteUmResponsavelCadastradoSemAlunoVinculado(String nomeResponsavel) {
        context.resetMensagens();
        excecao = null;
        aluno = null;
        responsavel = criarResponsavelUseCase.executar(
                nomeResponsavel,
                emailSeguro(nomeResponsavel) + ".sem.aluno@email.com"
        );
    }

    @Dado("que existe um responsavel {string} vinculado ao aluno {string}")
    public void queExisteUmResponsavelVinculadoAoAluno(String nomeResponsavel, String nomeAluno) {
        context.resetMensagens();
        excecao = null;
        aluno = criarAlunoUseCase.executar(nomeAluno, emailSeguro(nomeAluno) + ".excluir.aluno@email.com");
        responsavel = criarResponsavelUseCase.executar(
                nomeResponsavel,
                emailSeguro(nomeResponsavel) + ".excluir.resp@email.com"
        );
        vincularResponsavelUseCase.executar(aluno.getId(), responsavel.getId(), true, true, true);
    }

    @Dado("que nao existe responsavel com o id informado para exclusao")
    public void queNaoExisteResponsavelComOIdInformadoParaExclusao() {
        context.resetMensagens();
        excecao = null;
        responsavelIdInexistente = 999999L;
    }

    @Quando("o coordenador exclui o responsavel {string}")
    public void oCoordenadorExcluiOResponsavel(String nomeResponsavel) {
        try {
            excluirResponsavelUseCase.executar(responsavel.getId());
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("o coordenador tenta excluir o responsavel inexistente")
    public void oCoordenadorTentaExcluirOResponsavelInexistente() {
        try {
            excluirResponsavelUseCase.executar(responsavelIdInexistente);
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Entao("o sistema confirma a exclusao do responsavel")
    public void oSistemaConfirmaAExclusaoDoResponsavel() {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
    }

    @Entao("o aluno {string} nao possui mais responsavel vinculado")
    public void oAlunoNaoPossuiMaisResponsavelVinculado(String nomeAluno) {
        assertNotNull(aluno);
        Aluno alunoAtualizado = alunoRepository.buscarPorId(aluno.getId()).orElseThrow();
        assertNull(alunoAtualizado.getResponsavelId());
    }

    @Entao("o sistema informa que o responsavel nao foi encontrado")
    public void oSistemaInformaQueOResponsavelNaoFoiEncontrado() {
        assertNotNull(excecao);
        assertNotNull(context.getMensagem());
        assertTrue(context.getMensagem().toLowerCase().contains("responsavel")
                || context.getMensagem().toLowerCase().contains("responsável"));
    }

    private String emailSeguro(String nome) {
        return nome.toLowerCase().replace(" ", ".");
    }
}
