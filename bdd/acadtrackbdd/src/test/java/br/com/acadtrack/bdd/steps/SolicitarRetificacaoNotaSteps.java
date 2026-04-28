package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import br.com.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import br.com.acadtrack.aplicacao.nota.LancarNotaUseCase;
import br.com.acadtrack.aplicacao.retificacao.AprovarRetificacaoUseCase;
import br.com.acadtrack.aplicacao.retificacao.IniciarAnaliseRetificacaoUseCase;
import br.com.acadtrack.aplicacao.retificacao.ReprovarRetificacaoUseCase;
import br.com.acadtrack.aplicacao.retificacao.SolicitarRetificacaoUseCase;
import br.com.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import br.com.acadtrack.bdd.support.TestContext;
import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.aluno.AlunoRepository;
import br.com.acadtrack.dominioacademico.disciplina.Disciplina;
import br.com.acadtrack.dominioavaliacao.nota.Nota;
import br.com.acadtrack.dominioavaliacao.nota.NotaRepository;
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
    private final SolicitarRetificacaoUseCase solicitarRetificacaoUseCase;
    private final IniciarAnaliseRetificacaoUseCase iniciarAnaliseRetificacaoUseCase;
    private final AprovarRetificacaoUseCase aprovarRetificacaoUseCase;
    private final ReprovarRetificacaoUseCase reprovarRetificacaoUseCase;
    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;

    private Aluno aluno;
    private Disciplina disciplina;
    private Simulado simulado;
    private Nota nota;
    private SolicitacaoRetificacao retificacao;
    private Exception excecao;
    private double valorOriginalNota;

    public SolicitarRetificacaoNotaSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            CriarSimuladoUseCase criarSimuladoUseCase,
            LancarNotaUseCase lancarNotaUseCase,
            SolicitarRetificacaoUseCase solicitarRetificacaoUseCase,
            IniciarAnaliseRetificacaoUseCase iniciarAnaliseRetificacaoUseCase,
            AprovarRetificacaoUseCase aprovarRetificacaoUseCase,
            ReprovarRetificacaoUseCase reprovarRetificacaoUseCase,
            NotaRepository notaRepository,
            AlunoRepository alunoRepository
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.lancarNotaUseCase = lancarNotaUseCase;
        this.solicitarRetificacaoUseCase = solicitarRetificacaoUseCase;
        this.iniciarAnaliseRetificacaoUseCase = iniciarAnaliseRetificacaoUseCase;
        this.aprovarRetificacaoUseCase = aprovarRetificacaoUseCase;
        this.reprovarRetificacaoUseCase = reprovarRetificacaoUseCase;
        this.notaRepository = notaRepository;
        this.alunoRepository = alunoRepository;
    }

    @Dado("que o aluno {string} possui uma nota lançada")
    public void queOAlunoPossuiUmaNotaLancada(String nomeAluno) {
        context.resetMensagens();
        excecao = null;
        retificacao = null;

        aluno = criarAlunoUseCase.executar(nomeAluno, nomeAluno + "@email.com");
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

    @Quando("ele solicita retificação informando a justificativa {string}")
    public void eleSolicitaRetificacaoInformandoAJustificativa(String justificativa) {
        solicitarRetificacao(justificativa);
    }

    private void solicitarRetificacao(String justificativa) {
        try {
            retificacao = solicitarRetificacaoUseCase.executar(nota.getId(), justificativa);
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
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Dado("a solicitação está em análise")
    public void aSolicitacaoEstaEmAnalise() {
        oResponsavelIniciaAAnaliseDaSolicitacaoDeRetificacao();
        assertTrue(context.isOperacaoExecutada());
        assertEquals(SolicitacaoRetificacao.STATUS_EM_ANALISE, retificacao.getStatus());
    }

    @Quando("o responsável aprova a retificação alterando a nota para {double} com justificativa {string}")
    public void oResponsavelAprovaARetificacaoAlterandoANotaPara(Double novoValor, String justificativaDecisao) {
        try {
            retificacao = aprovarRetificacaoUseCase.executar(retificacao.getId(), novoValor, justificativaDecisao);
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
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
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
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

    @Entao("o sistema informa que já existe solicitação de retificação em aberto para esta nota")
    public void oSistemaInformaQueJaExisteSolicitacaoDeRetificacaoEmAbertoParaEstaNota() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Já existe solicitação de retificação em aberto para esta nota", context.getMensagem());
    }

    @Entao("o sistema atualiza a solicitação de retificação para status {string}")
    public void oSistemaAtualizaASolicitacaoDeRetificacaoParaStatus(String statusEsperado) {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertEquals(statusEsperado, retificacao.getStatus());
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

    @Entao("a nota do aluno permanece com o valor original")
    public void aNotaDoAlunoPermaneceComOValorOriginal() {
        Nota notaAtualizada = notaRepository.buscarPorId(nota.getId())
                .orElseThrow(() -> new AssertionError("Nota não encontrada após reprovação da retificação"));
        assertEquals(valorOriginalNota, notaAtualizada.getValor(), 0.001);
    }
}