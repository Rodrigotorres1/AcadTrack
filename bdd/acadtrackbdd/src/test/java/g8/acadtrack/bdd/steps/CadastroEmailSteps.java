package g8.acadtrack.bdd.steps;

import g8.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import g8.acadtrack.aplicacao.responsavel.CriarResponsavelUseCase;
import g8.acadtrack.bdd.support.TestContext;
import g8.acadtrack.dominiocompartilhado.excecao.ConflitoDeEstadoException;
import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CadastroEmailSteps {

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarResponsavelUseCase criarResponsavelUseCase;

    private Exception excecao;

    public CadastroEmailSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarResponsavelUseCase criarResponsavelUseCase
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarResponsavelUseCase = criarResponsavelUseCase;
    }

    @Dado("que já existe aluno com e-mail {string}")
    public void queJaExisteAlunoComEmail(String email) {
        criarAlunoUseCase.executar("Aluno Email Existente", email);
    }

    @Quando("o coordenador tenta cadastrar aluno sem e-mail")
    public void oCoordenadorTentaCadastrarAlunoSemEmail() {
        tentarCadastrar(() -> criarAlunoUseCase.executar("Aluno Sem Email", null));
    }

    @Quando("o coordenador tenta cadastrar aluno com e-mail {string}")
    public void oCoordenadorTentaCadastrarAlunoComEmail(String email) {
        tentarCadastrar(() -> criarAlunoUseCase.executar("Aluno Email Invalido", email));
    }

    @Quando("o coordenador tenta cadastrar responsável sem e-mail")
    public void oCoordenadorTentaCadastrarResponsavelSemEmail() {
        tentarCadastrar(() -> criarResponsavelUseCase.executar("Responsável Sem Email", null));
    }

    @Quando("o coordenador tenta cadastrar responsável com e-mail {string}")
    public void oCoordenadorTentaCadastrarResponsavelComEmail(String email) {
        tentarCadastrar(() -> criarResponsavelUseCase.executar("Responsável Email Invalido", email));
    }

    @Entao("o sistema informa que o e-mail é obrigatório")
    public void oSistemaInformaQueOEmailEObrigatorio() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertInstanceOf(RegraDeNegocioException.class, excecao);
        assertEquals("Email é obrigatório", context.getMensagem());
    }

    @Entao("o sistema informa que o e-mail é inválido")
    public void oSistemaInformaQueOEmailEInvalido() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertInstanceOf(RegraDeNegocioException.class, excecao);
        assertEquals("Email inválido", context.getMensagem());
    }

    @Entao("o sistema informa que o e-mail do aluno já está cadastrado")
    public void oSistemaInformaQueOEmailDoAlunoJaEstaCadastrado() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertInstanceOf(ConflitoDeEstadoException.class, excecao);
        assertEquals("Já existe aluno cadastrado com este e-mail", context.getMensagem());
    }

    private void tentarCadastrar(Runnable acao) {
        context.resetMensagens();
        excecao = null;
        try {
            acao.run();
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }
}
