package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.ranking.GerarRankingUseCase;
import br.com.acadtrack.dominioavaliacao.ranking.ResultadoRanking;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rankings")
public class RankingController {

    private final GerarRankingUseCase gerarRankingUseCase;

    public RankingController(GerarRankingUseCase gerarRankingUseCase) {
        this.gerarRankingUseCase = gerarRankingUseCase;
    }

    @GetMapping("/{simuladoId}")
    public List<ResultadoRanking> gerar(@PathVariable Long simuladoId) {
        return gerarRankingUseCase.executar(simuladoId);
    }
}