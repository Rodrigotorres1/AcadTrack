package g8.acadtrack.apresentacao.controller;

import g8.acadtrack.aplicacao.simulado.VincularDisciplinaSimuladoUseCase;
import g8.acadtrack.apresentacao.dto.request.VincularDisciplinaSimuladoRequest;
import g8.acadtrack.apresentacao.dto.response.ErroApiResponse;
import g8.acadtrack.apresentacao.dto.response.SimuladoDisciplinaResponse;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Simulados", description = "Cadastro de simulados e composição com disciplinas.")
@RestController
@RequestMapping("/simulados")
public class SimuladoDisciplinaController {

    private final VincularDisciplinaSimuladoUseCase vincularDisciplinaSimuladoUseCase;

    public SimuladoDisciplinaController(VincularDisciplinaSimuladoUseCase vincularDisciplinaSimuladoUseCase) {
        this.vincularDisciplinaSimuladoUseCase = vincularDisciplinaSimuladoUseCase;
    }

    @Operation(summary = "Acrescentar disciplina ao simulado com peso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Componente criado",
                    content = @Content(schema = @Schema(implementation = SimuladoDisciplinaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validação ou regra",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Simulado/disciplina não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PostMapping("/{simuladoId}/disciplinas")
    public ResponseEntity<SimuladoDisciplinaResponse> vincular(
            @Parameter(description = "Simulado") @PathVariable Long simuladoId,
            @RequestBody @Valid VincularDisciplinaSimuladoRequest request
    ) {
        SimuladoDisciplina resultado = vincularDisciplinaSimuladoUseCase.executar(
                simuladoId,
                request.getDisciplinaId(),
                request.getPeso()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SimuladoDisciplinaResponse.fromDomain(resultado));
    }
}
