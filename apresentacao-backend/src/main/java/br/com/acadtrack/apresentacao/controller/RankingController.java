package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.ranking.GerarRankingUseCase;
import br.com.acadtrack.aplicacao.ranking.RankingItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rankings")
public class RankingController {

    private final GerarRankingUseCase gerarRankingUseCase;

    public RankingController(GerarRankingUseCase gerarRankingUseCase) {
        this.gerarRankingUseCase = gerarRankingUseCase;
    }

    @GetMapping("/{simuladoId}")
    public List<RankingItem> gerar(@PathVariable Long simuladoId) {
        return gerarRankingUseCase.executar(simuladoId);
    }
}