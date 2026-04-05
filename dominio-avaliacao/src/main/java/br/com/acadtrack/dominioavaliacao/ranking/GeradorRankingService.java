package br.com.acadtrack.dominioavaliacao.ranking;

import br.com.acadtrack.dominioavaliacao.nota.Nota;

import java.util.*;
import java.util.stream.Collectors;

public class GeradorRankingService {

    public List<ResultadoRanking> gerarRanking(List<Nota> notas) {

        Map<Long, Double> mediaPorAluno = new HashMap<>();

        Map<Long, List<Nota>> notasAgrupadas = notas.stream()
                .collect(Collectors.groupingBy(Nota::getAlunoId));

        for (Map.Entry<Long, List<Nota>> entry : notasAgrupadas.entrySet()) {
            double media = entry.getValue()
                    .stream()
                    .mapToDouble(Nota::getValor)
                    .average()
                    .orElse(0.0);

            mediaPorAluno.put(entry.getKey(), media);
        }

        return mediaPorAluno.entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .map(e -> new ResultadoRanking(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }
}