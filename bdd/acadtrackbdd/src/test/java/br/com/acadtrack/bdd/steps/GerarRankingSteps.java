package br.com.acadtrack.bdd.steps;

import br.com.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import br.com.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import br.com.acadtrack.aplicacao.nota.LancarNotaUseCase;
import br.com.acadtrack.aplicacao.ranking.GerarRankingUseCase;
import br.com.acadtrack.aplicacao.ranking.RankingItem;
import br.com.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import br.com.acadtrack.aplicacao.simulado.VincularDisciplinaSimuladoUseCase;
import br.com.acadtrack.bdd.support.TestContext;
import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.disciplina.Disciplina;
import br.com.acadtrack.dominioavaliacao.simulado.Simulado;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GerarRankingSteps {

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarSimuladoUseCase criarSimuladoUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final VincularDisciplinaSimuladoUseCase vincularDisciplinaSimuladoUseCase;
    private final LancarNotaUseCase lancarNotaUseCase;
    private final GerarRankingUseCase gerarRankingUseCase;

    private Simulado simulado;
    private List<RankingItem> ranking;
    private final Map<Long, String> nomesPorAlunoId = new HashMap<>();

    public GerarRankingSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarSimuladoUseCase criarSimuladoUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            VincularDisciplinaSimuladoUseCase vincularDisciplinaSimuladoUseCase,
            LancarNotaUseCase lancarNotaUseCase,
            GerarRankingUseCase gerarRankingUseCase
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.vincularDisciplinaSimuladoUseCase = vincularDisciplinaSimuladoUseCase;
        this.lancarNotaUseCase = lancarNotaUseCase;
        this.gerarRankingUseCase = gerarRankingUseCase;
    }

    @Dado("que existem alunos com notas lançadas no simulado")
    public void queExistemAlunosComNotasLancadasNoSimulado() {
        context.resetMensagens();
        nomesPorAlunoId.clear();

        simulado = criarSimuladoUseCase.executar("Simulado ranking", null);
        Disciplina disciplina = criarDisciplinaUseCase.executar("Matemática");
        vincularDisciplinaSimuladoUseCase.executar(simulado.getId(), disciplina.getId(), 1.0);

        Aluno joao = criarAlunoUseCase.executar("João Silva", "joao@email.com");
        Aluno maria = criarAlunoUseCase.executar("Maria Souza", "maria@email.com");
        Aluno pedro = criarAlunoUseCase.executar("Pedro Lima", "pedro@email.com");

        nomesPorAlunoId.put(joao.getId(), joao.getNome());
        nomesPorAlunoId.put(maria.getId(), maria.getNome());
        nomesPorAlunoId.put(pedro.getId(), pedro.getNome());

        lancarNotaUseCase.executar(joao.getId(), simulado.getId(), disciplina.getId(), 8.5);
        lancarNotaUseCase.executar(maria.getId(), simulado.getId(), disciplina.getId(), 9.0);
        lancarNotaUseCase.executar(pedro.getId(), simulado.getId(), disciplina.getId(), 7.0);
    }

    @Dado("que não existem notas lançadas no simulado")
    public void queNaoExistemNotasLancadasNoSimulado() {
        context.resetMensagens();
        nomesPorAlunoId.clear();

        simulado = criarSimuladoUseCase.executar("Simulado sem notas", null);
    }

    @Quando("o sistema gera o ranking")
    public void oSistemaGeraORanking() {
        ranking = gerarRankingUseCase.executar(simulado.getId());
        context.setOperacaoExecutada(true);
    }

    @Quando("o sistema tenta gerar o ranking")
    public void oSistemaTentaGerarORanking() {
        oSistemaGeraORanking();
    }

    @Então("os alunos são ordenados do maior para o menor desempenho")
    public void osAlunosSaoOrdenadosDoMaiorParaOMenorDesempenho() {
        assertTrue(context.isOperacaoExecutada());
        assertEquals(3, ranking.size());
        assertEquals("Maria Souza", nomesPorAlunoId.get(ranking.get(0).alunoId()));
        assertEquals("João Silva", nomesPorAlunoId.get(ranking.get(1).alunoId()));
        assertEquals("Pedro Lima", nomesPorAlunoId.get(ranking.get(2).alunoId()));
    }

    @Então("o sistema informa que não há dados suficientes para gerar o ranking")
    public void oSistemaInformaQueNaoHaDadosSuficientesParaGerarORanking() {
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(ranking);
        assertTrue(ranking.isEmpty());
    }
}