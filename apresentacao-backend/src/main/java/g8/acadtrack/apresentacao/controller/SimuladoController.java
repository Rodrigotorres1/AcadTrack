package g8.acadtrack.apresentacao.controller;

import g8.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import g8.acadtrack.apresentacao.dto.request.CriarSimuladoRequest;
import g8.acadtrack.apresentacao.dto.response.ErroApiResponse;
import g8.acadtrack.apresentacao.dto.response.SimuladoResponse;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Simulados", description = "Cadastro de simulados e composição com disciplinas.")
@RestController
@RequestMapping("/simulados")
public class SimuladoController {

    private final CriarSimuladoUseCase criarSimuladoUseCase;


    public SimuladoController(CriarSimuladoUseCase criarSimuladoUseCase) {
        this.criarSimuladoUseCase = criarSimuladoUseCase;
    }

    @Operation(summary = "Criar simulado",
            description = "Define descrição e disciplinas. Substitua **disciplinasIds** pelos `id` retornados em `POST /disciplinas` (ex.: base nova costuma gerar `1` e `2`).",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CriarSimuladoRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "demo_simulado_ids_explicitos",
                                            summary = "Ajuste disciplinasIds ao que foi criado antes",
                                            value = "{\"descricao\":\"Simulado Demo Swagger\",\"disciplinasIds\":[1,2]}"),
                                    @ExampleObject(
                                            name = "um_curso_so",
                                            summary = "Uma disciplina só",
                                            value = "{\"descricao\":\"Simulado rapido Swagger\",\"disciplinasIds\":[1]}")
                            }
                    ))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado",
                    content = @Content(schema = @Schema(implementation = SimuladoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validação ou regra de composição",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PostMapping
    public ResponseEntity<SimuladoResponse> criar(@RequestBody @Valid CriarSimuladoRequest dto) {
        Simulado simulado = criarSimuladoUseCase.executar(
                dto.getDescricao(),
                dto.getDisciplinasIds()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SimuladoResponse(simulado.getId(), simulado.getDescricao()));
    }
}