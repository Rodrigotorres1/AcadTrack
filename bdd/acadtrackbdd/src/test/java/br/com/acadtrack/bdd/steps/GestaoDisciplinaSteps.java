package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import br.com.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import br.com.acadtrack.aplicacao.disciplina.InativarDisciplinaUseCase;
import br.com.acadtrack.aplicacao.nota.LancarNotaUseCase;
import br.com.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import br.com.acadtrack.bdd.support.TestContext;
import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.disciplina.Disciplina;
import br.com.acadtrack.dominioavaliacao.simulado.Simulado;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GestaoDisciplinaSteps {

    private final TestContext context;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final InativarDisciplinaUseCase inativarDisciplinaUseCase;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarSimuladoUseCase criarSimuladoUseCase;
    private final LancarNotaUseCase lancarNotaUseCase;

    private Disciplina disciplina;
    private Exception excecao;

    public GestaoDisciplinaSteps(
            TestContext context,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            InativarDisciplinaUseCase inativarDisciplinaUseCase,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarSimuladoUseCase criarSimuladoUseCase,
            LancarNotaUseCase lancarNotaUseCase
    ) {
        this.context = context;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.inativarDisciplinaUseCase = inativarDisciplinaUseCase;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.lancarNotaUseCase = lancarNotaUseCase;
    }

    @Quando("o coordenador cria a disciplina {string}")
    public void oCoordenadorCriaADisciplina(String nomeDisciplina) {
        context.resetMensagens();
        excecao = null;
        try {
            disciplina = criarDisciplinaUseCase.executar(nomeDisciplina);
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema registra a disciplina como {string}")
    public void oSistemaRegistraADisciplinaComo(String statusEsperado) {
        assertNotNull(disciplina);
        assertEquals(statusEsperado, disciplina.getStatus().name());
    }

    @Dado("que já existe a disciplina {string}")
    public void queJaExisteADisciplina(String nomeDisciplina) {
        context.resetMensagens();
        excecao = null;
        criarDisciplinaUseCase.executar(nomeDisciplina);
    }

    @Então("o sistema informa que já existe disciplina com esse nome")
    public void oSistemaInformaQueJaExisteDisciplinaComEsseNome() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Já existe disciplina cadastrada com este nome", context.getMensagem());
    }

    @Dado("que existe uma disciplina inativa chamada {string}")
    public void queExisteUmaDisciplinaInativaChamada(String nomeDisciplina) {
        context.resetMensagens();
        excecao = null;
        Disciplina criada = criarDisciplinaUseCase.executar(nomeDisciplina);
        disciplina = inativarDisciplinaUseCase.executar(criada.getId());
    }

    @Quando("o professor tenta lançar nota para essa disciplina inativa")
    public void oProfessorTentaLancarNotaParaEssaDisciplinaInativa() {
        try {
            Disciplina disciplinaBase = criarDisciplinaUseCase.executar("Base Nota");
            Simulado simulado = criarSimuladoUseCase.executar("Simulado de Nota", List.of(disciplinaBase.getId()));
            Aluno aluno = criarAlunoUseCase.executar("Aluno Nota", "aluno.nota@email.com");

            lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), disciplina.getId(), 7.0);
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema informa que disciplina inativa não pode receber nota")
    public void oSistemaInformaQueDisciplinaInativaNaoPodeReceberNota() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Disciplina inativa não pode receber lançamento de nota", context.getMensagem());
    }

    @Quando("o coordenador tenta criar simulado com essa disciplina inativa")
    public void oCoordenadorTentaCriarSimuladoComEssaDisciplinaInativa() {
        try {
            criarSimuladoUseCase.executar("Simulado Inválido", List.of(disciplina.getId()));
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema informa que disciplina inativa não pode ser vinculada a simulado")
    public void oSistemaInformaQueDisciplinaInativaNaoPodeSerVinculadaASimulado() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Disciplina inativa não pode ser vinculada a simulado", context.getMensagem());
    }
}
