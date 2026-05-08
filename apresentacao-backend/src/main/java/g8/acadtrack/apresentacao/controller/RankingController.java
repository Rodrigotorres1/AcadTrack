package g8.acadtrack.apresentacao.controller;

import g8.acadtrack.aplicacao.ranking.CriterioRankingAcademico;
import g8.acadtrack.aplicacao.ranking.GerarRankingAcademicoUseCase;
import g8.acadtrack.aplicacao.ranking.GerarRankingUseCase;
import g8.acadtrack.aplicacao.ranking.RankingItem;
import g8.acadtrack.apresentacao.dto.response.ErroApiResponse;
import g8.acadtrack.apresentacao.dto.response.RankingAcademicoResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Rankings", description = "Ranking por simulado (alternativa atual ao ranking legado em /notas).")
@RestController
@RequestMapping("/rankings")
public class RankingController {

    private final GerarRankingUseCase gerarRankingUseCase;
    private final GerarRankingAcademicoUseCase gerarRankingAcademicoUseCase;

    public RankingController(
            GerarRankingUseCase gerarRankingUseCase,
            GerarRankingAcademicoUseCase gerarRankingAcademicoUseCase
    ) {
        this.gerarRankingUseCase = gerarRankingUseCase;
        this.gerarRankingAcademicoUseCase = gerarRankingAcademicoUseCase;
    }

    @Operation(summary = "Ranking acadêmico geral",
            description = "Apoio da análise de desempenho. Percorre a coleção ordenada por Iterator na aplicação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista pode ser vazia",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = RankingAcademicoResponse.class))))
    })
    @GetMapping
    public List<RankingAcademicoResponse> gerarRankingAcademico(
            @RequestParam(defaultValue = "10") int limite,
            @RequestParam(defaultValue = "MEDIA_DESC") CriterioRankingAcademico criterio
    ) {
        return gerarRankingAcademicoUseCase.executar(limite, criterio)
                .stream()
                .map(RankingAcademicoResponse::fromApplication)
                .toList();
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
