package g8.acadtrack.aplicacao.nota;

import g8.acadtrack.aplicacao.nota.AvaliacaoAcademicaService.SimuladoDisciplinaKey;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CalcularMediaPonderadaUseCase {

    private final NotaRepository notaRepository;
    private final SimuladoDisciplinaRepository simuladoDisciplinaRepository;
    private final AvaliacaoAcademicaService avaliacaoAcademicaService;

    public CalcularMediaPonderadaUseCase(
            NotaRepository notaRepository,
            SimuladoDisciplinaRepository simuladoDisciplinaRepository,
            AvaliacaoAcademicaService avaliacaoAcademicaService
    ) {
        this.notaRepository = notaRepository;
        this.simuladoDisciplinaRepository = simuladoDisciplinaRepository;
        this.avaliacaoAcademicaService = avaliacaoAcademicaService;
    }

    public double executar(Long alunoId, Long simuladoId) {
        List<Nota> notas = notaRepository.buscarPorAlunoESimulado(alunoId, simuladoId);
        List<SimuladoDisciplina> disciplinas = simuladoDisciplinaRepository.buscarPorSimulado(simuladoId);
        Map<SimuladoDisciplinaKey, Double> pesosPorSimuladoEDisciplina = disciplinas.stream()
                .collect(Collectors.groupingBy(
                        disciplina -> new SimuladoDisciplinaKey(
                                disciplina.getSimuladoId(),
                                disciplina.getDisciplinaId()
                        ),
                        Collectors.summingDouble(SimuladoDisciplina::getPeso)
                ));

        return avaliacaoAcademicaService.calcularMediaPonderada(notas, pesosPorSimuladoEDisciplina);
    }
}
