package br.com.acadtrack.aplicacao.ranking;

import br.com.acadtrack.dominioavaliacao.nota.Nota;
import br.com.acadtrack.dominioavaliacao.nota.NotaRepository;
import br.com.acadtrack.dominioavaliacao.ranking.GeradorRankingService;
import br.com.acadtrack.dominioavaliacao.ranking.ResultadoRanking;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GerarRankingUseCase {

    private final NotaRepository notaRepository;
    private final GeradorRankingService geradorRankingService;

    public GerarRankingUseCase(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
        this.geradorRankingService = new GeradorRankingService();
    }

    public List<ResultadoRanking> executar(Long simuladoId) {
        List<Nota> notas = notaRepository.buscarPorSimuladoId(simuladoId);
        return geradorRankingService.gerarRanking(notas);
    }
}