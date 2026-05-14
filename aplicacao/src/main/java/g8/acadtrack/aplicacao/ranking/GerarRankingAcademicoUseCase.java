package g8.acadtrack.aplicacao.ranking;

import g8.acadtrack.aplicacao.nota.AvaliacaoAcademicaService;
import g8.acadtrack.aplicacao.nota.AvaliacaoAcademicaService.SimuladoDisciplinaKey;
import g8.acadtrack.aplicacao.nota.risco.ClassificadorRiscoAcademicoService;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplinaRepository;
import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GerarRankingAcademicoUseCase {

    private static final double LIMIAR_BAIXO_DESEMPENHO_SIMULADO = 5.0;

    private final AlunoRepository alunoRepository;
    private final OrdenarRankingAcademicoService ordenarRankingAcademicoService;
    private final NotaRepository notaRepository;
    private final SimuladoDisciplinaRepository simuladoDisciplinaRepository;
    private final AvaliacaoAcademicaService avaliacaoAcademicaService;
    private final ClassificadorRiscoAcademicoService classificadorRiscoAcademicoService;

    public GerarRankingAcademicoUseCase(
            AlunoRepository alunoRepository,
            OrdenarRankingAcademicoService ordenarRankingAcademicoService,
            NotaRepository notaRepository,
            SimuladoDisciplinaRepository simuladoDisciplinaRepository,
            AvaliacaoAcademicaService avaliacaoAcademicaService,
            ClassificadorRiscoAcademicoService classificadorRiscoAcademicoService
    ) {
        this.alunoRepository = alunoRepository;
        this.ordenarRankingAcademicoService = ordenarRankingAcademicoService;
        this.notaRepository = notaRepository;
        this.simuladoDisciplinaRepository = simuladoDisciplinaRepository;
        this.avaliacaoAcademicaService = avaliacaoAcademicaService;
        this.classificadorRiscoAcademicoService = classificadorRiscoAcademicoService;
    }

    public List<RankingAcademicoItem> executar(int limite) {
        return executar(limite, CriterioRankingAcademico.MEDIA_DESC);
    }

    public List<RankingAcademicoItem> executar(int limite, CriterioRankingAcademico criterio) {
        List<RankingAcademicoItem> ordenados = ordenarRankingAcademicoService.ordenar(montarItens(), criterio);
        RankingAcademicoIterator iterator = new ListaRankingAcademicoIterator(ordenados);
        List<RankingAcademicoItem> resultado = new ArrayList<>();
        int quantidadeMaxima = limite <= 0 ? ordenados.size() : Math.min(limite, ordenados.size());

        while (iterator.hasNext() && resultado.size() < quantidadeMaxima) {
            resultado.add(iterator.next());
        }

        return resultado;
    }

    public int calcularPosicaoPorMedia(double mediaAritmetica) {
        return Math.toIntExact(alunoRepository.contarAlunosComNotasComMediaMaiorQue(mediaAritmetica) + 1);
    }

    public int contarParticipantes() {
        return Math.toIntExact(alunoRepository.contarAlunosComNotas());
    }

    private List<RankingAcademicoItem> montarItens() {
        List<Aluno> alunos = alunoRepository.buscarAlunosComNotas();
        if (alunos.isEmpty()) {
            return List.of();
        }

        List<Long> alunoIds = alunos.stream()
                .map(Aluno::getId)
                .toList();
        List<Nota> notas = notaRepository.buscarPorAlunoIds(alunoIds);
        Map<Long, List<Double>> mediasPonderadasPorAluno = calcularMediasPonderadasPorAluno(notas);

        return alunos.stream()
                .map(aluno -> mapearAluno(aluno, mediasPonderadasPorAluno.getOrDefault(aluno.getId(), List.of())))
                .toList();
    }

    private RankingAcademicoItem mapearAluno(Aluno aluno, List<Double> mediasPonderadasPorSimulado) {
        return new RankingAcademicoItem(
                aluno.getId(),
                aluno.getNome(),
                aluno.getMediaAritmetica(),
                0,
                aluno.getSituacaoAcademica().name(),
                classificarRisco(aluno, mediasPonderadasPorSimulado)
        );
    }

    private NivelRiscoAcademico classificarRisco(Aluno aluno, List<Double> mediasPonderadasPorSimulado) {
        long simuladosComBaixoDesempenho = contarSimuladosComBaixoDesempenho(mediasPonderadasPorSimulado);
        return classificadorRiscoAcademicoService.classificar(aluno.getMediaAritmetica(), simuladosComBaixoDesempenho);
    }

    private Map<Long, List<Double>> calcularMediasPonderadasPorAluno(List<Nota> notas) {
        if (notas.isEmpty()) {
            return Map.of();
        }

        Map<AlunoSimuladoKey, List<Nota>> notasPorAlunoESimulado = notas.stream()
                .collect(Collectors.groupingBy(nota -> new AlunoSimuladoKey(nota.getAlunoId(), nota.getSimuladoId())));
        Map<SimuladoDisciplinaKey, Double> pesosPorSimuladoEDisciplina = buscarPesosPorSimuladoEDisciplina(notas);

        return notasPorAlunoESimulado.entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getKey().alunoId(),
                        Collectors.mapping(
                                entry -> avaliacaoAcademicaService.calcularMediaPonderada(
                                        entry.getValue(),
                                        pesosPorSimuladoEDisciplina
                                ),
                                Collectors.toList()
                        )
                ));
    }

    private Map<SimuladoDisciplinaKey, Double> buscarPesosPorSimuladoEDisciplina(List<Nota> notas) {
        List<Long> simuladoIds = notas.stream()
                .map(Nota::getSimuladoId)
                .distinct()
                .toList();

        return simuladoDisciplinaRepository.buscarPorSimuladoIds(simuladoIds)
                .stream()
                .collect(Collectors.groupingBy(
                        simuladoDisciplina -> new SimuladoDisciplinaKey(
                                simuladoDisciplina.getSimuladoId(),
                                simuladoDisciplina.getDisciplinaId()
                        ),
                        Collectors.summingDouble(SimuladoDisciplina::getPeso)
                ));
    }

    private long contarSimuladosComBaixoDesempenho(List<Double> mediasPonderadasPorSimulado) {
        return mediasPonderadasPorSimulado.stream()
                .filter(mediaPonderada -> mediaPonderada < LIMIAR_BAIXO_DESEMPENHO_SIMULADO)
                .count();
    }

    private record AlunoSimuladoKey(Long alunoId, Long simuladoId) {
    }
}
