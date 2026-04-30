package g8.acadtrack.aplicacao.ranking;

import g8.acadtrack.aplicacao.nota.CalcularMediaPonderadaUseCase;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class GerarRankingUseCase {

    private final NotaRepository notaRepository;
    private final CalcularMediaPonderadaUseCase calcularMediaPonderadaUseCase;

    public GerarRankingUseCase(NotaRepository notaRepository,
                               CalcularMediaPonderadaUseCase calcularMediaPonderadaUseCase) {
        this.notaRepository = notaRepository;
        this.calcularMediaPonderadaUseCase = calcularMediaPonderadaUseCase;
    }

    public List<RankingItem> executar(Long simuladoId) {
        List<Nota> notas = notaRepository.buscarPorSimuladoId(simuladoId);

        Map<Long, Boolean> alunosDoSimulado = new HashMap<>();

        for (Nota nota : notas) {
            alunosDoSimulado.put(nota.getAlunoId(), true);
        }

        List<RankingItem> ranking = new ArrayList<>();

        for (Long alunoId : alunosDoSimulado.keySet()) {
            double mediaPonderada = calcularMediaPonderadaUseCase.executar(alunoId, simuladoId);
            ranking.add(new RankingItem(alunoId, mediaPonderada));
        }

        ranking.sort(Comparator.comparingDouble(RankingItem::media).reversed());

        return ranking;
    }
}