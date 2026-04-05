package br.com.acadtrack.aplicacao.nota;

import br.com.acadtrack.dominioavaliacao.nota.Nota;
import br.com.acadtrack.dominioavaliacao.nota.NotaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RankingAlunosUseCase {

    private final NotaRepository notaRepository;

    public RankingAlunosUseCase(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    public List<Map<String, Object>> executar() {
        List<Nota> todasNotas = notaRepository.buscarTodas();

        Map<Long, List<Double>> notasPorAluno = new HashMap<>();

        for (Nota nota : todasNotas) {
            notasPorAluno
                    .computeIfAbsent(nota.getAlunoId(), k -> new ArrayList<>())
                    .add(nota.getValor());
        }

        List<Map<String, Object>> ranking = new ArrayList<>();

        for (Map.Entry<Long, List<Double>> entry : notasPorAluno.entrySet()) {
            Long alunoId = entry.getKey();
            List<Double> notas = entry.getValue();

            double soma = 0.0;
            for (Double valor : notas) {
                soma += valor;
            }

            double media = soma / notas.size();

            Map<String, Object> item = new HashMap<>();
            item.put("alunoId", alunoId);
            item.put("media", media);

            ranking.add(item);
        }

        ranking.sort((a, b) -> Double.compare(
                (Double) b.get("media"),
                (Double) a.get("media")
        ));

        int posicao = 1;
        for (Map<String, Object> item : ranking) {
            item.put("posicao", posicao++);

            double media = (Double) item.get("media");
            if (media >= 7.0) {
                item.put("status", "APROVADO");
            } else {
                item.put("status", "REPROVADO");
            }
        }

        return ranking;
    }
}