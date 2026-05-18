package g8.acadtrack.aplicacao.ranking;

import g8.acadtrack.aplicacao.nota.AvaliacaoAcademicaService;
import g8.acadtrack.aplicacao.nota.AvaliacaoAcademicaService.SimuladoDisciplinaKey;
import g8.acadtrack.aplicacao.nota.risco.ClassificadorRiscoAcademicoService;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GerarRankingAcademicoUseCase {

    private final ContadorParticipantesRankingPort contadorParticipantesRankingPort;
    private final OrdenarRankingAcademicoService ordenarRankingAcademicoService;
    private final NotaRepository notaRepository;
    private final SimuladoRepository simuladoRepository;
    private final AvaliacaoAcademicaService avaliacaoAcademicaService;
    private final ClassificadorRiscoAcademicoService classificadorRiscoAcademicoService;

    public GerarRankingAcademicoUseCase(
            ContadorParticipantesRankingPort contadorParticipantesRankingPort,
            OrdenarRankingAcademicoService ordenarRankingAcademicoService,
            NotaRepository notaRepository,
            SimuladoRepository simuladoRepository,
            AvaliacaoAcademicaService avaliacaoAcademicaService,
            ClassificadorRiscoAcademicoService classificadorRiscoAcademicoService
    ) {
        this.contadorParticipantesRankingPort = contadorParticipantesRankingPort;
        this.ordenarRankingAcademicoService = ordenarRankingAcademicoService;
        this.notaRepository = notaRepository;
        this.simuladoRepository = simuladoRepository;
        this.avaliacaoAcademicaService = avaliacaoAcademicaService;
        this.classificadorRiscoAcademicoService = classificadorRiscoAcademicoService;
    }

    public List<RankingAcademicoItem> executar(int limite) {
        return executar(limite, CriterioRankingAcademico.MEDIA_DESC);
    }

    public List<RankingAcademicoItem> executar(int limite, CriterioRankingAcademico criterio) {
        List<RankingAcademicoItem> ordenados = ordenarRankingAcademicoService.ordenar(montarItens(), criterio);
        int quantidadeMaxima = limite <= 0 ? ordenados.size() : Math.min(limite, ordenados.size());

        return ordenados.stream()
                .limit(quantidadeMaxima)
                .toList();
    }

    private List<RankingAcademicoItem> montarItens() {
        List<Aluno> alunos = contadorParticipantesRankingPort.buscarParticipantes();
        if (alunos.isEmpty()) {
            return List.of();
        }

        List<Long> alunoIds = alunos.stream()
                .map(Aluno::getId)
                .toList();
        List<Nota> notas = notaRepository.buscarPorAlunoIds(alunoIds);
        Map<Long, List<Nota>> notasPorAluno = notas.stream()
                .collect(Collectors.groupingBy(Nota::getAlunoId));
        Map<Long, List<Double>> mediasPonderadasPorAluno = calcularMediasPonderadasPorAluno(notas);

        return alunos.stream()
                .map(aluno -> mapearAluno(
                        aluno,
                        notasPorAluno.getOrDefault(aluno.getId(), List.of()),
                        mediasPonderadasPorAluno.getOrDefault(aluno.getId(), List.of())
                ))
                .toList();
    }

    private RankingAcademicoItem mapearAluno(
            Aluno aluno,
            List<Nota> notasDoAluno,
            List<Double> mediasPonderadasPorSimulado
    ) {
        double mediaGeral = notasDoAluno.isEmpty()
                ? aluno.getMediaAritmetica()
                : avaliacaoAcademicaService.calcularMediaAritmetica(notasDoAluno);

        return new RankingAcademicoItem(
                aluno.getId(),
                aluno.getNome(),
                mediaGeral,
                0,
                avaliacaoAcademicaService.calcularSituacao(mediaGeral).name(),
                classificarRisco(mediaGeral, mediasPonderadasPorSimulado)
        );
    }

    private NivelRiscoAcademico classificarRisco(double mediaGeral, List<Double> mediasPonderadasPorSimulado) {
        long simuladosComBaixoDesempenho = contarSimuladosComBaixoDesempenho(mediasPonderadasPorSimulado);
        return classificadorRiscoAcademicoService.classificar(mediaGeral, simuladosComBaixoDesempenho);
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

        return simuladoRepository.buscarPesosDisciplinasPorSimuladoIds(simuladoIds)
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
                .filter(avaliacaoAcademicaService::isBaixoDesempenhoSimulado)
                .count();
    }

    private record AlunoSimuladoKey(Long alunoId, Long simuladoId) {
    }
}
