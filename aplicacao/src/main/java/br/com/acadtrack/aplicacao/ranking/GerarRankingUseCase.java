package br.com.acadtrack.aplicacao.ranking;

import br.com.acadtrack.dominioavaliacao.nota.Nota;
import br.com.acadtrack.dominioavaliacao.nota.NotaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GerarRankingUseCase {

    private final NotaRepository notaRepository;

    public GerarRankingUseCase(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    public List<RankingItem> executar(Long simuladoId) {
        List<Nota> notas = notaRepository.buscarPorSimuladoId(simuladoId);

        Map<Long, List<Double>> notasPorAluno = new HashMap<>();

        for (Nota nota : notas) {
            notasPorAluno
                    .computeIfAbsent(nota.getAlunoId(), k -> new ArrayList<>())
                    .add(nota.getValor());
        }

        List<RankingItem> ranking = new ArrayList<>();

        for (Map.Entry<Long, List<Double>> entry : notasPorAluno.entrySet()) {
            Long alunoId = entry.getKey();
            List<Double> valores = entry.getValue();

            double soma = 0;
            for (Double valor : valores) {
                soma += valor;
            }

            double media = valores.isEmpty() ? 0 : soma / valores.size();

            ranking.add(new RankingItem(alunoId, media));
        }

        ranking.sort(Comparator.comparingDouble(RankingItem::media).reversed());

        return ranking;
    }
}