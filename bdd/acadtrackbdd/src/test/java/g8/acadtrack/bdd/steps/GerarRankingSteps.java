package g8.acadtrack.bdd.steps;

import g8.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import g8.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import g8.acadtrack.aplicacao.nota.LancarNotaUseCase;
import g8.acadtrack.aplicacao.ranking.GerarRankingUseCase;
import g8.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import g8.acadtrack.bdd.support.TestContext;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GerarRankingSteps {

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final CriarSimuladoUseCase criarSimuladoUseCase;
    private final LancarNotaUseCase lancarNotaUseCase;
    private final GerarRankingUseCase gerarRankingUseCase;

    private Simulado simulado;
    private Disciplina disciplina;
    private Aluno aluno1;
    private Aluno aluno2;
    private Aluno aluno3;

    private List<?> ranking;
    private Exception excecao;

    public GerarRankingSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            CriarSimuladoUseCase criarSimuladoUseCase,
            LancarNotaUseCase lancarNotaUseCase,
            GerarRankingUseCase gerarRankingUseCase
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.lancarNotaUseCase = lancarNotaUseCase;
        this.gerarRankingUseCase = gerarRankingUseCase;
    }

    @Dado("que existem alunos com notas lançadas no simulado")
    public void queExistemAlunosComNotasLancadasNoSimulado() {
        context.resetMensagens();
        ranking = new ArrayList<>();
        excecao = null;

        disciplina = criarDisciplinaUseCase.executar("Matemática");
        simulado = criarSimuladoUseCase.executar("Simulado Ranking", List.of(disciplina.getId()));

        aluno1 = criarAlunoUseCase.executar("João Silva", "joao@email.com");
        aluno2 = criarAlunoUseCase.executar("Maria Souza", "maria@email.com");
        aluno3 = criarAlunoUseCase.executar("Pedro Lima", "pedro@email.com");

        lancarNotaUseCase.executar(aluno1.getId(), simulado.getId(), disciplina.getId(), 8.5);
        lancarNotaUseCase.executar(aluno2.getId(), simulado.getId(), disciplina.getId(), 9.0);
        lancarNotaUseCase.executar(aluno3.getId(), simulado.getId(), disciplina.getId(), 7.0);
    }

    @Dado("que não existem notas lançadas no simulado")
    public void queNaoExistemNotasLancadasNoSimulado() {
        context.resetMensagens();
        ranking = new ArrayList<>();
        excecao = null;

        disciplina = criarDisciplinaUseCase.executar("Matemática");
        simulado = criarSimuladoUseCase.executar("Simulado Vazio", List.of(disciplina.getId()));
    }

    @Quando("o sistema gera o ranking")
    public void oSistemaGeraORanking() {
        try {
            ranking = gerarRankingUseCase.executar(simulado.getId());
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("o sistema tenta gerar o ranking")
    public void oSistemaTentaGerarORanking() {
        oSistemaGeraORanking();
    }

    @Então("os alunos são ordenados do maior para o menor desempenho")
    public void osAlunosSaoOrdenadosDoMaiorParaOMenorDesempenho() {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertNotNull(ranking);
        assertEquals(3, ranking.size());
    }

    @Então("o sistema retorna um ranking vazio")
    public void oSistemaRetornaUmRankingVazio() {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertNotNull(ranking);
        assertTrue(ranking.isEmpty());
    }
}