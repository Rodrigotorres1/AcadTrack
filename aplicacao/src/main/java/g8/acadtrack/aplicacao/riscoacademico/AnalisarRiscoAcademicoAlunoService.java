package g8.acadtrack.aplicacao.riscoacademico;

import g8.acadtrack.aplicacao.nota.AvaliacaoAcademicaService;
import g8.acadtrack.aplicacao.nota.AvaliacaoAcademicaService.SimuladoDisciplinaKey;
import g8.acadtrack.aplicacao.nota.risco.ClassificadorRiscoAcademicoService;
import g8.acadtrack.dominioacademico.aluno.SituacaoAcademica;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalisarRiscoAcademicoAlunoService {

    private final AvaliacaoAcademicaService avaliacaoAcademicaService;
    private final SimuladoRepository simuladoRepository;
    private final ClassificadorRiscoAcademicoService classificadorRiscoAcademicoService;

    public AnalisarRiscoAcademicoAlunoService(
            AvaliacaoAcademicaService avaliacaoAcademicaService,
            SimuladoRepository simuladoRepository,
            ClassificadorRiscoAcademicoService classificadorRiscoAcademicoService
    ) {
        this.avaliacaoAcademicaService = avaliacaoAcademicaService;
        this.simuladoRepository = simuladoRepository;
        this.classificadorRiscoAcademicoService = classificadorRiscoAcademicoService;
    }

    public AnaliseRiscoAcademicoResultado analisar(Long alunoId, List<Nota> notas) {
        double mediaGeral = avaliacaoAcademicaService.calcularMediaAritmetica(notas);
        SituacaoAcademica situacaoAcademica = avaliacaoAcademicaService.calcularSituacao(mediaGeral);
        long simuladosComBaixoDesempenho = contarSimuladosComBaixoDesempenho(notas);
        NivelRiscoAcademico nivelRisco = classificadorRiscoAcademicoService.classificar(
                mediaGeral,
                simuladosComBaixoDesempenho
        );

        return new AnaliseRiscoAcademicoResultado(
                alunoId,
                mediaGeral,
                simuladosComBaixoDesempenho,
                nivelRisco != NivelRiscoAcademico.BAIXO,
                nivelRisco,
                situacaoAcademica
        );
    }

    private long contarSimuladosComBaixoDesempenho(List<Nota> notas) {
        Map<Long, List<Nota>> notasPorSimulado = notas.stream()
                .collect(Collectors.groupingBy(Nota::getSimuladoId));
        Map<SimuladoDisciplinaKey, Double> pesosPorSimuladoEDisciplina = carregarPesosPorSimuladoEDisciplina(
                notasPorSimulado.keySet().stream().toList()
        );

        return notasPorSimulado.values()
                .stream()
                .mapToDouble(notasDoSimulado -> avaliacaoAcademicaService.calcularMediaPonderada(
                        notasDoSimulado,
                        pesosPorSimuladoEDisciplina
                ))
                .filter(avaliacaoAcademicaService::isBaixoDesempenhoSimulado)
                .count();
    }

    private Map<SimuladoDisciplinaKey, Double> carregarPesosPorSimuladoEDisciplina(List<Long> simuladoIds) {
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
}
