package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import br.com.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import br.com.acadtrack.aplicacao.nota.LancarNotaUseCase;
import br.com.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import br.com.acadtrack.bdd.support.TestContext;
import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.aluno.AlunoRepository;
import br.com.acadtrack.dominioacademico.aluno.SituacaoAcademica;
import br.com.acadtrack.dominioacademico.disciplina.Disciplina;
import br.com.acadtrack.dominioavaliacao.nota.Nota;
import br.com.acadtrack.dominioavaliacao.simulado.Simulado;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LancarNotaSteps {

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final CriarSimuladoUseCase criarSimuladoUseCase;
    private final LancarNotaUseCase lancarNotaUseCase;
    private final AlunoRepository alunoRepository;

    private Aluno aluno;
    private Disciplina disciplina;
    private Disciplina segundaDisciplina;
    private Simulado simulado;
    private Simulado segundoSimulado;
    private Nota notaLancada;
    private Exception excecao;

    public LancarNotaSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            CriarSimuladoUseCase criarSimuladoUseCase,
            LancarNotaUseCase lancarNotaUseCase,
            AlunoRepository alunoRepository
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.lancarNotaUseCase = lancarNotaUseCase;
        this.alunoRepository = alunoRepository;
    }

    @Dado("que o aluno {string} realizou o simulado")
    public void queOAlunoRealizouOSimulado(String nomeAluno) {
        context.resetMensagens();
        excecao = null;
        notaLancada = null;

        aluno = criarAlunoUseCase.executar(nomeAluno, nomeAluno + "@email.com");
        disciplina = criarDisciplinaUseCase.executar("Matemática");

        simulado = criarSimuladoUseCase.executar(
                "Simulado de lançamento de nota",
                List.of(disciplina.getId())
        );
    }

    @Dado("que o aluno {string} possui nota {double} já lançada")
    public void queOAlunoPossuiNotaJaLancada(String nomeAluno, Double valorNota) {
        queOAlunoRealizouOSimulado(nomeAluno);
        notaLancada = lancarNotaUseCase.executar(
                aluno.getId(),
                simulado.getId(),
                disciplina.getId(),
                valorNota
        );

        segundaDisciplina = criarDisciplinaUseCase.executar("Português " + nomeAluno);
        segundoSimulado = criarSimuladoUseCase.executar(
                "Simulado complementar " + nomeAluno,
                List.of(segundaDisciplina.getId())
        );
    }

    @Quando("o professor lança a nota {double} para o aluno {string}")
    public void oProfessorLancaANotaParaOAluno(Double valorNota, String nomeAluno) {
        try {
            notaLancada = lancarNotaUseCase.executar(
                    aluno.getId(),
                    simulado.getId(),
                    disciplina.getId(),
                    valorNota
            );
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("o professor já lançou a nota {double} para o aluno {string}")
    public void oProfessorJaLancouANotaParaOAluno(Double valorNota, String nomeAluno) {
        oProfessorLancaANotaParaOAluno(valorNota, nomeAluno);
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(notaLancada);
    }

    @Quando("o professor tenta lançar a nota {double} novamente para o mesmo aluno e disciplina")
    public void oProfessorTentaLancarANotaNovamenteParaOMesmoAlunoEDisciplina(Double valorNota) {
        try {
            lancarNotaUseCase.executar(
                    aluno.getId(),
                    simulado.getId(),
                    disciplina.getId(),
                    valorNota
            );
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("o professor lança uma nova nota {double} para o aluno {string} em outra disciplina")
    public void oProfessorLancaUmaNovaNotaParaOAlunoEmOutraDisciplina(Double valorNota, String nomeAluno) {
        try {
            notaLancada = lancarNotaUseCase.executar(
                    aluno.getId(),
                    segundoSimulado.getId(),
                    segundaDisciplina.getId(),
                    valorNota
            );
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema registra a nota do aluno")
    public void oSistemaRegistraANotaDoAluno() {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertNotNull(notaLancada);
    }

    @Então("o sistema informa a nota deve estar entre 0 e 10")
    public void oSistemaInformaQueANotaEInvalida() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("A nota deve estar entre 0 e 10", context.getMensagem());
    }

    @Então("o sistema informa que já existe nota lançada para este aluno, simulado e disciplina")
    public void oSistemaInformaDuplicidadeDeNota() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Já existe nota lançada para este aluno, simulado e disciplina", context.getMensagem());
    }

    @Então("o sistema atualiza a média do aluno para {double}")
    public void oSistemaAtualizaAMediaDoAlunoPara(Double mediaEsperada) {
        Aluno alunoAtualizado = alunoRepository.buscarPorId(aluno.getId())
                .orElseThrow(() -> new AssertionError("Aluno não encontrado após lançamento de nota"));

        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertEquals(mediaEsperada, alunoAtualizado.getMediaAtual(), 0.001);
    }

    @Então("o sistema atualiza a situação acadêmica do aluno para {string}")
    public void oSistemaAtualizaASituacaoAcademicaDoAlunoPara(String situacaoEsperada) {
        Aluno alunoAtualizado = alunoRepository.buscarPorId(aluno.getId())
                .orElseThrow(() -> new AssertionError("Aluno não encontrado após lançamento de nota"));

        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertEquals(SituacaoAcademica.valueOf(situacaoEsperada), alunoAtualizado.getSituacaoAcademica());
    }
}