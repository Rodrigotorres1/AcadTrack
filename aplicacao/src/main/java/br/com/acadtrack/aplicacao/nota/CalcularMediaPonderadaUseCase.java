package br.com.acadtrack.aplicacao.nota;

import br.com.acadtrack.dominioavaliacao.nota.Nota;
import br.com.acadtrack.dominioavaliacao.nota.NotaRepository;
import br.com.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import br.com.acadtrack.dominioavaliacao.simulado.SimuladoDisciplinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

        double somaPonderada = 0;
        double somaPesos = 0;

        for (Nota nota : notas) {
            for (SimuladoDisciplina sd : disciplinas) {
                if (sd.getDisciplinaId().equals(nota.getDisciplinaId())) {
                    somaPonderada += nota.getValor() * sd.getPeso();
                    somaPesos += sd.getPeso();
                }
            }
        }

        if (somaPesos == 0) {
            return 0;
        }

        return avaliacaoAcademicaService.arredondarMedia(somaPonderada / somaPesos);
    }
}