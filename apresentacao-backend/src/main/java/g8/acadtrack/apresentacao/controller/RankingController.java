package g8.acadtrack.apresentacao.controller;

import g8.acadtrack.aplicacao.ranking.GerarRankingUseCase;
import g8.acadtrack.aplicacao.ranking.RankingItem;
import g8.acadtrack.apresentacao.dto.response.ErroApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Rankings", description = "Ranking por simulado (alternativa atual ao ranking legado em /notas).")
@RestController
@RequestMapping("/rankings")
public class RankingController {

    private final GerarRankingUseCase gerarRankingUseCase;

    public RankingController(GerarRankingUseCase gerarRankingUseCase) {
        this.gerarRankingUseCase = gerarRankingUseCase;
    }

    @Operation(summary = "Ranking de alunos por simulado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ordenação por média (lista pode ser vazia)",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = RankingItem.class)))),
            @ApiResponse(responseCode = "404", description = "Simulado não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "Regra ou argumento inválido",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @GetMapping("/{simuladoId}")
    public List<RankingItem> gerar(
            @Parameter(description = "Simulado") @PathVariable Long simuladoId) {
        return gerarRankingUseCase.executar(simuladoId);
    }
}