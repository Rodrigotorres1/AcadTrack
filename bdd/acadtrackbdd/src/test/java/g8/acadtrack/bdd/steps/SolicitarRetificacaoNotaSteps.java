package g8.acadtrack.bdd.steps;

import g8.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import g8.acadtrack.aplicacao.aluno.InativarAlunoUseCase;
import g8.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import g8.acadtrack.aplicacao.nota.LancarNotaUseCase;
import g8.acadtrack.aplicacao.retificacao.AprovarRetificacaoUseCase;
import g8.acadtrack.aplicacao.retificacao.IniciarAnaliseRetificacaoUseCase;
import g8.acadtrack.aplicacao.retificacao.ListarRetificacoesUseCase;
import g8.acadtrack.aplicacao.retificacao.ReprovarRetificacaoUseCase;
import g8.acadtrack.aplicacao.retificacao.SolicitacaoRetificacaoDetalheResultado;
import g8.acadtrack.aplicacao.retificacao.SolicitarRetificacaoUseCase;
import g8.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import g8.acadtrack.bdd.support.TestContext;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacaoRepository;
import g8.acadtrack.dominioavaliacao.retificacao.StatusSolicitacaoRetificacao;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import g8.acadtrack.dominiocompartilhado.excecao.ConflitoDeEstadoException;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SolicitarRetificacaoNotaSteps {

    private static final Long ID_INEXISTENTE = 999_999L;

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final InativarAlunoUseCase inativarAlunoUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final CriarSimuladoUseCase criarSimuladoUseCase;
    private final LancarNotaUseCase lancarNotaUseCase;
    private final SolicitarRetificacaoUseCase solicitarRetificacaoUseCase;
    private final IniciarAnaliseRetificacaoUseCase iniciarAnaliseRetificacaoUseCase;
    private final AprovarRetificacaoUseCase aprovarRetificacaoUseCase;
    private final ReprovarRetificacaoUseCase reprovarRetificacaoUseCase;
    private final ListarRetificacoesUseCase listarRetificacoesUseCase;
    private final SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository;
    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;

    private Aluno aluno;
    private Disciplina disciplina;
    private Simulado simulado;
    private Nota nota;
    private SolicitacaoRetificacao retificacao;
    private Exception excecao;
    private double valorOriginalNota;
    private List<SolicitacaoRetificacaoDetalheResultado> listaRetificacoes;

    public SolicitarRetificacaoNotaSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            InativarAlunoUseCase inativarAlunoUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            CriarSimuladoUseCase criarSimuladoUseCase,
            LancarNotaUseCase lancarNotaUseCase,
            SolicitarRetificacaoUseCase solicitarRetificacaoUseCase,
            IniciarAnaliseRetificacaoUseCase iniciarAnaliseRetificacaoUseCase,
            AprovarRetificacaoUseCase aprovarRetificacaoUseCase,
            ReprovarRetificacaoUseCase reprovarRetificacaoUseCase,
            ListarRetificacoesUseCase listarRetificacoesUseCase,
            SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository,
            NotaRepository notaRepository,
            AlunoRepository alunoRepository
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.inativarAlunoUseCase = inativarAlunoUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.lancarNotaUseCase = lancarNotaUseCase;
        this.solicitarRetificacaoUseCase = solicitarRetificacaoUseCase;
        this.iniciarAnaliseRetificacaoUseCase = iniciarAnaliseRetificacaoUseCase;
        this.aprovarRetificacaoUseCase = aprovarRetificacaoUseCase;
        this.reprovarRetificacaoUseCase = reprovarRetificacaoUseCase;
        this.listarRetificacoesUseCase = listarRetificacoesUseCase;
        this.solicitacaoRetificacaoRepository = solicitacaoRetificacaoRepository;
        this.notaRepository = notaRepository;
        this.alunoRepository = alunoRepository;
    }

    @Dado("que o aluno {string} possui uma nota lançada")
    public void queOAlunoPossuiUmaNotaLancada(String nomeAluno) {
        context.resetMensagens();
        excecao = null;
        retificacao = null;

        aluno = criarAlunoUseCase.executar(nomeAluno, emailSeguro(nomeAluno) + "@email.com");
        disciplina = criarDisciplinaUseCase.executar("Matemática " + nomeAluno);
        Disciplina segundaDisciplina = criarDisciplinaUseCase.executar("Português " + nomeAluno);

        simulado = criarSimuladoUseCase.executar(
                "Simulado de retificação " + nomeAluno,
                List.of(disciplina.getId(), segundaDisciplina.getId())
        );

        nota = lancarNotaUseCase.executar(
                aluno.getId(),
                simulado.getId(),
                disciplina.getId(),
                7.0
        );
        valorOriginalNota = nota.getValor();
    }

    @Dado("o aluno está inativo")
    public void oAlunoEstaInativo() {
        aluno = inativarAlunoUseCase.executar(aluno.getId());
        assertFalse(aluno.isAtivo());
    }

    @Quando("ele solicita retificação informando a justificativa {string}")
    public void eleSolicitaRetificacaoInformandoAJustificativa(String justificativa) {
        solicitarRetificacao(justificativa);
    }

    private void solicitarRetificacao(String justificativa) {
        try {
            retificacao = solicitarRetificacaoUseCase.executar(nota.getId(), justificativa);
            context.setOperacaoExecutada(true);
        } catch (RegraDeNegocioException | ConflitoDeEstadoException | EntidadeNaoEncontradaException e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("ele solicita retificação sem justificativa")
    public void eleSolicitaRetificacaoSemJustificativa() {
        eleSolicitaRetificacaoInformandoAJustificativa("");
    }

    @Quando("ele solicita retificação para uma nota inexistente")
    public void eleSolicitaRetificacaoParaUmaNotaInexistente() {
        prepararTentativaSemDados();

        try {
            retificacao = solicitarRetificacaoUseCase.executar(ID_INEXISTENTE, "Revisar nota inexistente");
            context.setOperacaoExecutada(true);
        } catch (RegraDeNegocioException | ConflitoDeEstadoException | EntidadeNaoEncontradaException e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Dado("ele já possui uma solicitação de retificação em aberto para essa nota")
    public void eleJaPossuiUmaSolicitacaoDeRetificacaoEmAbertoParaEssaNota() {
        eleSolicitaRetificacaoInformandoAJustificativa("Primeira solicitação");
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(retificacao);
    }

    @Quando("ele tenta solicitar nova retificação para a mesma nota")
    public void eleTentaSolicitarNovaRetificacaoParaAMesmaNota() {
        eleSolicitaRetificacaoInformandoAJustificativa("Segunda solicitação para mesma nota");
    }

    @Quando("o responsável inicia a análise da solicitação de retificação")
    public void oResponsavelIniciaAAnaliseDaSolicitacaoDeRetificacao() {
        try {
            retificacao = iniciarAnaliseRetificacaoUseCase.executar(retificacao.getId());
            context.setOperacaoExecutada(true);
        } catch (EntidadeNaoEncontradaException | ConflitoDeEstadoException e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("o responsável inicia análise de uma solicitação de retificação inexistente")
    public void oResponsavelIniciaAnaliseDeUmaSolicitacaoDeRetificacaoInexistente() {
        prepararTentativaSemDados();

        try {
            retificacao = iniciarAnaliseRetificacaoUseCase.executar(ID_INEXISTENTE);
            context.setOperacaoExecutada(true);
        } catch (EntidadeNaoEncontradaException | ConflitoDeEstadoException e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Dado("a solicitação está em análise")
    public void aSolicitacaoEstaEmAnalise() {
        oResponsavelIniciaAAnaliseDaSolicitacaoDeRetificacao();
        assertTrue(context.isOperacaoExecutada());
        assertEquals(StatusSolicitacaoRetificacao.EM_ANALISE, retificacao.getStatus());
    }

    @Quando("o responsável aprova a retificação alterando a nota para {double} com justificativa {string}")
    public void oResponsavelAprovaARetificacaoAlterandoANotaPara(Double novoValor, String justificativaDecisao) {
        try {
            retificacao = aprovarRetificacaoUseCase.executar(retificacao.getId(), novoValor, justificativaDecisao);
            context.setOperacaoExecutada(true);
        } catch (EntidadeNaoEncontradaException | ConflitoDeEstadoException | RegraDeNegocioException e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("o responsável aprova uma solicitação de retificação inexistente")
    public void oResponsavelAprovaUmaSolicitacaoDeRetificacaoInexistente() {
        prepararTentativaSemDados();

        try {
            retificacao = aprovarRetificacaoUseCase.executar(ID_INEXISTENTE, 9.0, "Erro confirmado");
            context.setOperacaoExecutada(true);
        } catch (EntidadeNaoEncontradaException | ConflitoDeEstadoException | RegraDeNegocioException e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("o responsável reprova a solicitação de retificação com justificativa {string}")
    public void oResponsavelReprovaASolicitacaoDeRetificacao(String justificativaDecisao) {
        try {
            retificacao = reprovarRetificacaoUseCase.executar(retificacao.getId(), justificativaDecisao);
            context.setOperacaoExecutada(true);
        } catch (EntidadeNaoEncontradaException | ConflitoDeEstadoException | RegraDeNegocioException e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("o responsável reprova uma solicitação de retificação inexistente")
    public void oResponsavelReprovaUmaSolicitacaoDeRetificacaoInexistente() {
        prepararTentativaSemDados();

        try {
            retificacao = reprovarRetificacaoUseCase.executar(ID_INEXISTENTE, "Não foi identificado erro");
            context.setOperacaoExecutada(true);
        } catch (EntidadeNaoEncontradaException | ConflitoDeEstadoException | RegraDeNegocioException e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Entao("o sistema registra a solicitação de retificação com status {string}")
    public void oSistemaRegistraASolicitacaoDeRetificacaoComStatus(String statusEsperado) {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertEquals(statusEsperado, retificacao.getStatus().name());
    }

    @Entao("o sistema informa que a justificativa é obrigatória")
    public void oSistemaInformaQueAJustificativaEObrigatoria() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertInstanceOf(RegraDeNegocioException.class, excecao);
        assertEquals("Justificativa é obrigatória", context.getMensagem());
    }

    @Entao("o sistema informa que a nota não foi encontrada")
    public void oSistemaInformaQueANotaNaoFoiEncontrada() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertInstanceOf(EntidadeNaoEncontradaException.class, excecao);
        assertEquals("Nota não encontrada", context.getMensagem());
    }

    @Entao("o sistema informa que já existe solicitação de retificação em aberto para esta nota")
    public void oSistemaInformaQueJaExisteSolicitacaoDeRetificacaoEmAbertoParaEstaNota() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Já existe solicitação de retificação em aberto para esta nota", context.getMensagem());
    }

    @Entao("o sistema informa que a solicitação de retificação não foi encontrada")
    public void oSistemaInformaQueASolicitacaoDeRetificacaoNaoFoiEncontrada() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertInstanceOf(EntidadeNaoEncontradaException.class, excecao);
        assertEquals("Solicitação de retificação não encontrada", context.getMensagem());
    }

    @Entao("o sistema informa que a solicitação deve estar em análise para aprovação")
    public void oSistemaInformaQueASolicitacaoDeveEstarEmAnaliseParaAprovacao() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertInstanceOf(ConflitoDeEstadoException.class, excecao);
        assertEquals("A solicitação deve estar em análise para aprovação", context.getMensagem());
    }

    @Entao("o sistema informa que a solicitação deve estar em análise para reprovação")
    public void oSistemaInformaQueASolicitacaoDeveEstarEmAnaliseParaReprovacao() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertInstanceOf(ConflitoDeEstadoException.class, excecao);
        assertEquals("A solicitação deve estar em análise para reprovação", context.getMensagem());
    }

    @Entao("o sistema informa que a solicitação deve estar pendente para iniciar análise")
    public void oSistemaInformaQueASolicitacaoDeveEstarPendenteParaIniciarAnalise() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertInstanceOf(ConflitoDeEstadoException.class, excecao);
        assertEquals("A solicitação deve estar pendente para iniciar análise", context.getMensagem());
    }

    @Entao("o sistema atualiza a solicitação de retificação para status {string}")
    public void oSistemaAtualizaASolicitacaoDeRetificacaoParaStatus(String statusEsperado) {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertEquals(statusEsperado, retificacao.getStatus().name());
    }

    @Entao("a solicitação permanece com status {string}")
    public void aSolicitacaoPermaneceComStatus(String statusEsperado) {
        SolicitacaoRetificacao retificacaoPersistida = solicitacaoRetificacaoRepository.buscarPorId(retificacao.getId())
                .orElseThrow(() -> new AssertionError("Solicitação de retificação não encontrada após validação de estado"));

        assertEquals(StatusSolicitacaoRetificacao.valueOf(statusEsperado), retificacaoPersistida.getStatus());
    }

    @Entao("a nota do aluno é atualizada para {double}")
    public void aNotaDoAlunoEAtualizadaPara(Double valorEsperado) {
        Nota notaAtualizada = notaRepository.buscarPorId(nota.getId())
                .orElseThrow(() -> new AssertionError("Nota não encontrada após aprovação da retificação"));
        assertEquals(valorEsperado, notaAtualizada.getValor(), 0.001);
    }

    @Entao("a situação acadêmica do aluno é atualizada automaticamente")
    public void aSituacaoAcademicaDoAlunoEAtualizadaAutomaticamente() {
        Aluno alunoAtualizado = alunoRepository.buscarPorId(aluno.getId())
                .orElseThrow(() -> new AssertionError("Aluno não encontrado após aprovação da retificação"));
        assertNotNull(alunoAtualizado.getSituacaoAcademica());
    }

    @Quando("o responsável tenta aprovar a retificação sem justificativa de decisão")
    public void oResponsavelTentaAprovarARetificacaoSemJustificativaDeDecisao() {
        oResponsavelAprovaARetificacaoAlterandoANotaPara(9.0, "");
    }

    @Quando("o responsável tenta reprovar a retificação sem justificativa de decisão")
    public void oResponsavelTentaReprovarARetificacaoSemJustificativaDeDecisao() {
        oResponsavelReprovaASolicitacaoDeRetificacao("");
    }

    @Entao("o sistema informa que a justificativa da decisão é obrigatória")
    public void oSistemaInformaQueAJustificativaDaDecisaoEObrigatoria() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertInstanceOf(RegraDeNegocioException.class, excecao);
        assertEquals("Justificativa da decisão é obrigatória", context.getMensagem());
    }

    @Entao("a nota do aluno permanece com o valor original")
    public void aNotaDoAlunoPermaneceComOValorOriginal() {
        Nota notaAtualizada = notaRepository.buscarPorId(nota.getId())
                .orElseThrow(() -> new AssertionError("Nota não encontrada após reprovação da retificação"));
        assertEquals(valorOriginalNota, notaAtualizada.getValor(), 0.001);
    }

    @Dado("que foram criadas {int} retificações com alunos e notas distintos")
    public void queForamCriadasRetificacoesComAlunosENotasDistintos(int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            String sufixo = "Listagem" + i;
            Aluno a = criarAlunoUseCase.executar("Aluno " + sufixo, "aluno." + sufixo + "@email.com");
            Disciplina d1 = criarDisciplinaUseCase.executar("Disciplina A " + sufixo);
            Disciplina d2 = criarDisciplinaUseCase.executar("Disciplina B " + sufixo);
            Simulado s = criarSimuladoUseCase.executar("Simulado " + sufixo, List.of(d1.getId(), d2.getId()));
            Nota n = lancarNotaUseCase.executar(a.getId(), s.getId(), d1.getId(), 6.0 + i);
            solicitarRetificacaoUseCase.executar(n.getId(), "Justificativa " + sufixo);
        }
    }

    @Quando("o sistema lista todas as retificações")
    public void oSistemaListaTodasAsRetificacoes() {
        listaRetificacoes = listarRetificacoesUseCase.executar();
    }

    @Entao("a lista retorna {int} retificações")
    public void aListaRetorna(int quantidade) {
        assertEquals(quantidade, listaRetificacoes.size());
    }

    @Entao("cada retificação possui nome de aluno, disciplina e simulado preenchidos")
    public void cadaRetificacaoPossuiNomeDeAlunoDisciplinaESimuladoPreenchidos() {
        for (SolicitacaoRetificacaoDetalheResultado resultado : listaRetificacoes) {
            assertNotNull(resultado.alunoNome());
            assertNotEquals("-", resultado.alunoNome());
            assertNotNull(resultado.disciplinaNome());
            assertNotEquals("-", resultado.disciplinaNome());
            assertNotNull(resultado.simuladoDescricao());
            assertNotEquals("-", resultado.simuladoDescricao());
            assertNotNull(resultado.notaAtual());
        }
    }

    private String emailSeguro(String nomeAluno) {
        return nomeAluno.toLowerCase().replace(" ", ".");
    }

    private void prepararTentativaSemDados() {
        context.resetMensagens();
        excecao = null;
        retificacao = null;
    }
}
