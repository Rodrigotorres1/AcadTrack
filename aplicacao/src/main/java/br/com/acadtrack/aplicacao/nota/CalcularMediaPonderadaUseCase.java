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

    public CalcularMediaPonderadaUseCase(
            NotaRepository notaRepository,
            SimuladoDisciplinaRepository simuladoDisciplinaRepository
    ) {
        this.notaRepository = notaRepository;
        this.simuladoDisciplinaRepository = simuladoDisciplinaRepository;
    }

    public double executar(Long alunoId, Long simuladoId) {

        List<Nota> notas = notaRepository.buscarPorAlunoESimulado(alunoId, simuladoId);
        List<SimuladoDisciplina> disciplinas = simuladoDisciplinaRepository.buscarPorSimulado(simuladoId);

        double somaPesos = 0;
        double soma = 0;

        for (Nota nota : notas) {
            for (SimuladoDisciplina sd : disciplinas) {
                if (sd.getDisciplinaId().equals(nota.getDisciplinaId())) {
                    soma += nota.getValor() * sd.getPeso();
                    somaPesos += sd.getPeso();
                }
            }
        }

        return somaPesos == 0 ? 0 : soma / somaPesos;
    }
}