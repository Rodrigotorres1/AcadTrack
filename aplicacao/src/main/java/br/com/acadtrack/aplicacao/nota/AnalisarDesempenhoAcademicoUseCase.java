package br.com.acadtrack.aplicacao.nota;

import br.com.acadtrack.dominioavaliacao.nota.Nota;
import br.com.acadtrack.dominioavaliacao.nota.NotaRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AnalisarDesempenhoAcademicoUseCase {

    private static final double LIMIAR_MEDIA_RISCO = 6.0;
    private static final double LIMIAR_BAIXO_DESEMPENHO_SIMULADO = 5.0;
    private static final double LIMIAR_RISCO_ALTO_MEDIA = 5.0;

    private final NotaRepository notaRepository;
    private final CalcularMediaPonderadaUseCase calcularMediaPonderadaUseCase;
    private final AvaliacaoAcademicaService avaliacaoAcademicaService;

    public AnalisarDesempenhoAcademicoUseCase(
            NotaRepository notaRepository,
            CalcularMediaPonderadaUseCase calcularMediaPonderadaUseCase,
            AvaliacaoAcademicaService avaliacaoAcademicaService
    ) {
        this.notaRepository = notaRepository;
        this.calcularMediaPonderadaUseCase = calcularMediaPonderadaUseCase;
        this.avaliacaoAcademicaService = avaliacaoAcademicaService;
    }

    public AnaliseDesempenhoAcademicoResultado executar(Long alunoId) {
        List<Nota> notas = notaRepository.buscarPorAlunoId(alunoId);

        if (notas.isEmpty()) {
            throw new IllegalStateException("Aluno sem notas para análise de desempenho");
        }

        double mediaGeral = avaliacaoAcademicaService.calcularMedia(notas);

        List<AnaliseDesempenhoAcademicoResultado.MediaSimulado> historicoSimulados = notas.stream()
                .map(Nota::getSimuladoId)
                .distinct()
                .sorted(Comparator.naturalOrder())
                .map(simuladoId -> {
                    double mediaPonderada = calcularMediaPonderadaUseCase.executar(alunoId, simuladoId);
                    boolean baixoDesempenho = mediaPonderada < LIMIAR_BAIXO_DESEMPENHO_SIMULADO;
                    return new AnaliseDesempenhoAcademicoResultado.MediaSimulado(
                            simuladoId,
                            mediaPonderada,
                            baixoDesempenho
                    );
                })
                .toList();

        long simuladosComBaixoDesempenho = historicoSimulados.stream()
                .filter(AnaliseDesempenhoAcademicoResultado.MediaSimulado::baixoDesempenho)
                .count();

        boolean riscoAcademico = mediaGeral < LIMIAR_MEDIA_RISCO || simuladosComBaixoDesempenho > 0;

        String nivelRisco = determinarNivelRisco(mediaGeral, simuladosComBaixoDesempenho);
        String alerta = riscoAcademico
                ? "ALERTA: risco acadêmico identificado para o aluno"
                : "Desempenho acadêmico estável";

        return new AnaliseDesempenhoAcademicoResultado(
                alunoId,
                mediaGeral,
                notas.size(),
                historicoSimulados.size(),
                simuladosComBaixoDesempenho,
                riscoAcademico,
                nivelRisco,
                alerta,
                historicoSimulados
        );
    }

    private String determinarNivelRisco(double mediaGeral, long simuladosComBaixoDesempenho) {
        if (mediaGeral < LIMIAR_RISCO_ALTO_MEDIA || simuladosComBaixoDesempenho >= 2) {
            return "ALTO";
        }
        if (mediaGeral < LIMIAR_MEDIA_RISCO || simuladosComBaixoDesempenho == 1) {
            return "MODERADO";
        }
        return "BAIXO";
    }
}
