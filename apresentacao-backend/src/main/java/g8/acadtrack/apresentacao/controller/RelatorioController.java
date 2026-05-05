package g8.acadtrack.apresentacao.controller;

import g8.acadtrack.aplicacao.relatorio.CriterioOrdenacaoRelatorio;
import g8.acadtrack.aplicacao.relatorio.GerarRelatorioDesempenhoAcademicoUseCase;
import g8.acadtrack.apresentacao.dto.response.ErroApiResponse;
import g8.acadtrack.apresentacao.dto.response.RelatorioDesempenhoAcademicoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Relatorios", description = "Relatorios academicos consolidados.")
@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final GerarRelatorioDesempenhoAcademicoUseCase gerarRelatorioDesempenhoAcademicoUseCase;

    public RelatorioController(GerarRelatorioDesempenhoAcademicoUseCase gerarRelatorioDesempenhoAcademicoUseCase) {
        this.gerarRelatorioDesempenhoAcademicoUseCase = gerarRelatorioDesempenhoAcademicoUseCase;
    }

    @Operation(summary = "Relatorio de desempenho academico",
            description = "Ordenacao aceita: MAIOR_RISCO, MENOR_MEDIA ou MELHOR_MEDIA. Sem parametro, usa MAIOR_RISCO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatorio gerado",
                    content = @Content(schema = @Schema(implementation = RelatorioDesempenhoAcademicoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Criterio de ordenacao invalido",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @GetMapping("/desempenho-academico")
    public ResponseEntity<RelatorioDesempenhoAcademicoResponse> gerarRelatorioDesempenhoAcademico(
            @Parameter(description = "Criterio de ordenacao") @RequestParam(required = false) CriterioOrdenacaoRelatorio ordenacao
    ) {
        return ResponseEntity.ok(
                RelatorioDesempenhoAcademicoResponse.fromApplication(
                        gerarRelatorioDesempenhoAcademicoUseCase.executar(ordenacao)
                )
        );
    }
}
