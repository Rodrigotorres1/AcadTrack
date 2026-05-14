package g8.acadtrack.aplicacao.nota;

import g8.acadtrack.aplicacao.ranking.GerarRankingAcademicoUseCase;
import g8.acadtrack.aplicacao.nota.AvaliacaoAcademicaService.SimuladoDisciplinaKey;
import g8.acadtrack.aplicacao.nota.risco.ClassificadorRiscoAcademicoService;
import g8.acadtrack.dominioacademico.aluno.SituacaoAcademica;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplinaRepository;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalisarDesempenhoAcademicoUseCase extends FluxoAnaliseAcademicaTemplate {

    private static final double LIMIAR_BAIXO_DESEMPENHO_SIMULADO = 5.0;

    private final NotaRepository notaRepository;
    private final AvaliacaoAcademicaService avaliacaoAcademicaService;
    private final SimuladoRepository simuladoRepository;
    private final SimuladoDisciplinaRepository simuladoDisciplinaRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final GerarRankingAcademicoUseCase gerarRankingAcademicoUseCase;
    private final ClassificadorRiscoAcademicoService classificadorRiscoAcademicoService;

    public AnalisarDesempenhoAcademicoUseCase(
            NotaRepository notaRepository,
            AvaliacaoAcademicaService avaliacaoAcademicaService,
            SimuladoRepository simuladoRepository,
            SimuladoDisciplinaRepository simuladoDisciplinaRepository,
            DisciplinaRepository disciplinaRepository,
            GerarRankingAcademicoUseCase gerarRankingAcademicoUseCase,
            ClassificadorRiscoAcademicoService classificadorRiscoAcademicoService
    ) {
        this.notaRepository = notaRepository;
        this.avaliacaoAcademicaService = avaliacaoAcademicaService;
        this.simuladoRepository = simuladoRepository;
        this.simuladoDisciplinaRepository = simuladoDisciplinaRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.gerarRankingAcademicoUseCase = gerarRankingAcademicoUseCase;
        this.classificadorRiscoAcademicoService = classificadorRiscoAcademicoService;
    }

    @Override
    protected List<Nota> buscarNotas(Long alunoId) {
        return notaRepository.buscarPorAlunoId(alunoId);
    }

    @Override
    protected double calcularMediaGeral(List<Nota> notas) {
        return avaliacaoAcademicaService.calcularMediaAritmetica(notas);
    }

    @Override
    protected SituacaoAcademica calcularSituacaoAcademica(double mediaGeral) {
        return avaliacaoAcademicaService.calcularSituacao(mediaGeral);
    }

    @Override
    protected AnaliseDesempenhoAcademicoResultado montarResultado(
            Long alunoId,
            List<Nota> notas,
            double mediaGeral,
            SituacaoAcademica situacaoAcademica
    ) {
        return montarResultado(alunoId, notas, mediaGeral, situacaoAcademica, true);
    }

    public AnaliseDesempenhoAcademicoResultado executarSemRanking(Long alunoId) {
        List<Nota> notas = buscarNotas(alunoId);
        validarNotas(notas);
        double mediaGeral = calcularMediaGeral(notas);
        SituacaoAcademica situacaoAcademica = calcularSituacaoAcademica(mediaGeral);
        return montarResultado(alunoId, notas, mediaGeral, situacaoAcademica, false);
    }

    private AnaliseDesempenhoAcademicoResultado montarResultado(
            Long alunoId,
            List<Nota> notas,
            double mediaGeral,
            SituacaoAcademica situacaoAcademica,
            boolean incluirRanking
    ) {
        Map<Long, List<Nota>> notasPorSimulado = notas.stream()
                .collect(Collectors.groupingBy(Nota::getSimuladoId));
        Map<Long, String> nomesSimulados = carregarNomesSimulados(
                notasPorSimulado.keySet().stream().toList()
        );
        Map<SimuladoDisciplinaKey, Double> pesosPorSimuladoEDisciplina = carregarPesosPorSimuladoEDisciplina(
                notasPorSimulado.keySet().stream().toList()
        );

        List<AnaliseDesempenhoAcademicoResultado.MediaSimulado> historicoSimulados = notasPorSimulado.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> mediaSimulado(
                        entry.getKey(),
                        entry.getValue(),
                        nomesSimulados,
                        pesosPorSimuladoEDisciplina
                ))
                .toList();

        long simuladosComBaixoDesempenho = historicoSimulados.stream()
                .filter(AnaliseDesempenhoAcademicoResultado.MediaSimulado::baixoDesempenho)
                .count();

        NivelRiscoAcademico nivelRisco = classificadorRiscoAcademicoService.classificar(mediaGeral, simuladosComBaixoDesempenho);
        boolean riscoAcademico = nivelRisco != NivelRiscoAcademico.BAIXO;

        Map<Long, List<Nota>> notasAgrupadasPorDisciplina = notas.stream()
                .collect(Collectors.groupingBy(Nota::getDisciplinaId));
        Map<Long, String> nomesDisciplinas = carregarNomesDisciplinas(
                notasAgrupadasPorDisciplina.keySet().stream().toList()
        );

        List<AnaliseDesempenhoAcademicoResultado.MediaDisciplina> notasPorDisciplina = notasAgrupadasPorDisciplina
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> mediaDisciplina(entry.getKey(), entry.getValue(), nomesDisciplinas))
                .toList();
        Integer posicaoRanking = incluirRanking
                ? gerarRankingAcademicoUseCase.calcularPosicaoPorMedia(mediaGeral)
                : null;
        int totalAlunosRanking = incluirRanking
                ? gerarRankingAcademicoUseCase.contarParticipantes()
                : 0;
        boolean alunoNoTop10 = posicaoRanking != null && posicaoRanking <= 10;
        String mensagemRanking = incluirRanking
                ? mensagemRanking(posicaoRanking, alunoNoTop10)
                : "Ranking calculado sob demanda na consulta de desempenho";

        return new AnaliseDesempenhoAcademicoResultado(
                alunoId,
                mediaGeral,
                notas.size(),
                historicoSimulados.size(),
                simuladosComBaixoDesempenho,
                riscoAcademico,
                nivelRisco,
                mensagemAlerta(nivelRisco),
                situacaoAcademica,
                posicaoRanking,
                totalAlunosRanking,
                alunoNoTop10,
                mensagemRanking,
                historicoSimulados,
                notasPorDisciplina
        );
    }

    private AnaliseDesempenhoAcademicoResultado.MediaSimulado mediaSimulado(
            Long simuladoId,
            List<Nota> notas,
            Map<Long, String> nomesSimulados,
            Map<SimuladoDisciplinaKey, Double> pesosPorSimuladoEDisciplina
    ) {
        double mediaPonderada = avaliacaoAcademicaService.calcularMediaPonderada(notas, pesosPorSimuladoEDisciplina);
        boolean baixoDesempenho = mediaPonderada < LIMIAR_BAIXO_DESEMPENHO_SIMULADO;

        return new AnaliseDesempenhoAcademicoResultado.MediaSimulado(
                simuladoId,
                nomesSimulados.getOrDefault(simuladoId, "Simulado " + simuladoId),
                notas.size(),
                mediaPonderada,
                baixoDesempenho
        );
    }

    private AnaliseDesempenhoAcademicoResultado.MediaDisciplina mediaDisciplina(
            Long disciplinaId,
            List<Nota> notas,
            Map<Long, String> nomesDisciplinas
    ) {
        double media = avaliacaoAcademicaService.calcularMediaAritmetica(notas);
        SituacaoAcademica status = avaliacaoAcademicaService.calcularSituacao(media);

        return new AnaliseDesempenhoAcademicoResultado.MediaDisciplina(
                disciplinaId,
                nomesDisciplinas.getOrDefault(disciplinaId, "Disciplina " + disciplinaId),
                media,
                status,
                nivelRiscoDisciplina(status)
        );
    }

    private String mensagemAlerta(NivelRiscoAcademico nivelRisco) {
        return switch (nivelRisco) {
            case ALTO -> "Necessita intervenção pedagógica imediata";
            case MODERADO -> "Necessita atenção; acompanhamento recomendado";
            case BAIXO -> "Desempenho estável e satisfatório";
        };
    }

    private String mensagemRanking(Integer posicaoRanking, boolean alunoNoTop10) {
        if (posicaoRanking == null) {
            return "Aluno ainda não participa do ranking acadêmico";
        }
        if (alunoNoTop10) {
            return "Aluno em destaque no Top 10 acadêmico";
        }
        return "Aluno na posição " + posicaoRanking + " do ranking acadêmico";
    }

    private Map<Long, String> carregarNomesSimulados(List<Long> simuladoIds) {
        return simuladoRepository.buscarPorIds(simuladoIds)
                .stream()
                .collect(Collectors.toMap(
                        Simulado::getId,
                        Simulado::getDescricao,
                        (nomeAtual, nomeDuplicado) -> nomeAtual
                ));
    }

    private Map<Long, String> carregarNomesDisciplinas(List<Long> disciplinaIds) {
        return disciplinaRepository.buscarPorIds(disciplinaIds)
                .stream()
                .collect(Collectors.toMap(
                        Disciplina::getId,
                        Disciplina::getNome,
                        (nomeAtual, nomeDuplicado) -> nomeAtual
                ));
    }

    private Map<SimuladoDisciplinaKey, Double> carregarPesosPorSimuladoEDisciplina(List<Long> simuladoIds) {
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

    private NivelRiscoAcademico nivelRiscoDisciplina(SituacaoAcademica status) {
        return switch (status) {
            case APROVADO -> NivelRiscoAcademico.BAIXO;
            case RECUPERACAO -> NivelRiscoAcademico.MODERADO;
            case REPROVADO -> NivelRiscoAcademico.ALTO;
        };
    }
}
