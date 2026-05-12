package g8.acadtrack.aplicacao.nota;

import g8.acadtrack.aplicacao.ranking.GerarRankingAcademicoUseCase;
import g8.acadtrack.aplicacao.ranking.RankingAcademicoItem;
import g8.acadtrack.aplicacao.nota.risco.ClassificadorRiscoAcademicoService;
import g8.acadtrack.dominioacademico.aluno.SituacaoAcademica;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalisarDesempenhoAcademicoUseCase extends FluxoAnaliseAcademicaTemplate {

    private static final double LIMIAR_BAIXO_DESEMPENHO_SIMULADO = 5.0;

    private final NotaRepository notaRepository;
    private final CalcularMediaPonderadaUseCase calcularMediaPonderadaUseCase;
    private final AvaliacaoAcademicaService avaliacaoAcademicaService;
    private final SimuladoRepository simuladoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final GerarRankingAcademicoUseCase gerarRankingAcademicoUseCase;
    private final ClassificadorRiscoAcademicoService classificadorRiscoAcademicoService;

    public AnalisarDesempenhoAcademicoUseCase(
            NotaRepository notaRepository,
            CalcularMediaPonderadaUseCase calcularMediaPonderadaUseCase,
            AvaliacaoAcademicaService avaliacaoAcademicaService,
            SimuladoRepository simuladoRepository,
            DisciplinaRepository disciplinaRepository,
            GerarRankingAcademicoUseCase gerarRankingAcademicoUseCase,
            ClassificadorRiscoAcademicoService classificadorRiscoAcademicoService
    ) {
        this.notaRepository = notaRepository;
        this.calcularMediaPonderadaUseCase = calcularMediaPonderadaUseCase;
        this.avaliacaoAcademicaService = avaliacaoAcademicaService;
        this.simuladoRepository = simuladoRepository;
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

        List<AnaliseDesempenhoAcademicoResultado.MediaSimulado> historicoSimulados = notasPorSimulado.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> mediaSimulado(alunoId, entry.getKey(), entry.getValue()))
                .toList();

        long simuladosComBaixoDesempenho = historicoSimulados.stream()
                .filter(AnaliseDesempenhoAcademicoResultado.MediaSimulado::baixoDesempenho)
                .count();

        String nivelRisco = classificadorRiscoAcademicoService.classificar(mediaGeral, simuladosComBaixoDesempenho);
        boolean riscoAcademico = !"BAIXO".equals(nivelRisco);

        List<AnaliseDesempenhoAcademicoResultado.MediaDisciplina> notasPorDisciplina = notas.stream()
                .collect(Collectors.groupingBy(Nota::getDisciplinaId))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> mediaDisciplina(entry.getKey(), entry.getValue()))
                .toList();
        List<RankingAcademicoItem> rankingAcademico = incluirRanking
                ? gerarRankingAcademicoUseCase.executar(0)
                : List.of();
        RankingAcademicoItem rankingAluno = rankingAcademico.stream()
                .filter(item -> item.alunoId().equals(alunoId))
                .findFirst()
                .orElse(null);
        Integer posicaoRanking = rankingAluno == null ? null : rankingAluno.posicao();
        boolean alunoNoTop10 = posicaoRanking != null && posicaoRanking <= 10;
        String mensagemRanking = incluirRanking
                ? mensagemRanking(posicaoRanking, alunoNoTop10)
                : "Ranking calculado sob demanda na consulta de desempenho.";

        return new AnaliseDesempenhoAcademicoResultado(
                alunoId,
                mediaGeral,
                notas.size(),
                historicoSimulados.size(),
                simuladosComBaixoDesempenho,
                riscoAcademico,
                nivelRisco,
                mensagemAlerta(nivelRisco),
                situacaoAcademica.name(),
                posicaoRanking,
                rankingAcademico.size(),
                alunoNoTop10,
                mensagemRanking,
                historicoSimulados,
                notasPorDisciplina
        );
    }

    private AnaliseDesempenhoAcademicoResultado.MediaSimulado mediaSimulado(
            Long alunoId,
            Long simuladoId,
            List<Nota> notas
    ) {
        double mediaPonderada = calcularMediaPonderadaUseCase.executar(alunoId, simuladoId);
        boolean baixoDesempenho = mediaPonderada < LIMIAR_BAIXO_DESEMPENHO_SIMULADO;

        return new AnaliseDesempenhoAcademicoResultado.MediaSimulado(
                simuladoId,
                nomeSimulado(simuladoId),
                notas.size(),
                mediaPonderada,
                baixoDesempenho
        );
    }

    private AnaliseDesempenhoAcademicoResultado.MediaDisciplina mediaDisciplina(Long disciplinaId, List<Nota> notas) {
        double media = avaliacaoAcademicaService.calcularMediaAritmetica(notas);
        SituacaoAcademica status = avaliacaoAcademicaService.calcularSituacao(media);

        return new AnaliseDesempenhoAcademicoResultado.MediaDisciplina(
                disciplinaId,
                nomeDisciplina(disciplinaId),
                media,
                status.name(),
                nivelRiscoDisciplina(status)
        );
    }

    private String mensagemAlerta(String nivelRisco) {
        return switch (nivelRisco) {
            case "ALTO" -> "Necessita intervencao pedagogica imediata.";
            case "MODERADO" -> "Necessita atencao. Acompanhamento recomendado.";
            default -> "Desempenho estavel e satisfatorio.";
        };
    }

    private String mensagemRanking(Integer posicaoRanking, boolean alunoNoTop10) {
        if (posicaoRanking == null) {
            return "Aluno ainda nao participa do ranking academico.";
        }
        if (alunoNoTop10) {
            return "Aluno em destaque no Top 10 academico.";
        }
        return "Aluno na posicao " + posicaoRanking + " do ranking academico.";
    }

    private String nomeSimulado(Long simuladoId) {
        return simuladoRepository.buscarPorId(simuladoId)
                .map(Simulado::getDescricao)
                .orElse("Simulado " + simuladoId);
    }

    private String nomeDisciplina(Long disciplinaId) {
        return disciplinaRepository.buscarPorId(disciplinaId)
                .map(Disciplina::getNome)
                .orElse("Disciplina " + disciplinaId);
    }

    private String nivelRiscoDisciplina(SituacaoAcademica status) {
        return switch (status) {
            case APROVADO -> "BAIXO";
            case RECUPERACAO -> "MODERADO";
            case REPROVADO -> "ALTO";
        };
    }
}
