package g8.acadtrack.apresentacao.controller;

import g8.acadtrack.aplicacao.simulado.AtualizarSimuladoUseCase;
import g8.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import g8.acadtrack.aplicacao.simulado.DetalharSimuladoUseCase;
import g8.acadtrack.aplicacao.simulado.ListarSimuladosComResumoUseCase;
import g8.acadtrack.apresentacao.dto.request.AtualizarSimuladoRequest;
import g8.acadtrack.apresentacao.dto.request.CriarSimuladoRequest;
import g8.acadtrack.apresentacao.dto.response.ErroApiResponse;
import g8.acadtrack.apresentacao.dto.response.SimuladoDetalheResponse;
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

import java.util.List;

@Tag(name = "Simulados", description = "Cadastro de simulados e composição com disciplinas.")
@RestController
@RequestMapping("/simulados")
public class SimuladoController {

    private final CriarSimuladoUseCase criarSimuladoUseCase;
    private final AtualizarSimuladoUseCase atualizarSimuladoUseCase;
    private final ListarSimuladosComResumoUseCase listarSimuladosComResumoUseCase;
    private final DetalharSimuladoUseCase detalharSimuladoUseCase;

    public SimuladoController(
            CriarSimuladoUseCase criarSimuladoUseCase,
            AtualizarSimuladoUseCase atualizarSimuladoUseCase,
            ListarSimuladosComResumoUseCase listarSimuladosComResumoUseCase,
            DetalharSimuladoUseCase detalharSimuladoUseCase
    ) {
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.atualizarSimuladoUseCase = atualizarSimuladoUseCase;
        this.listarSimuladosComResumoUseCase = listarSimuladosComResumoUseCase;
        this.detalharSimuladoUseCase = detalharSimuladoUseCase;
    }

    @Operation(summary = "Listar simulados cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista pode ser vazia")
    })
    @GetMapping
    public ResponseEntity<List<SimuladoResponse>> listar() {
        return ResponseEntity.ok(
                listarSimuladosComResumoUseCase.executar()
                        .stream()
                        .map(SimuladoResponse::fromResumo)
                        .toList()
        );
    }

    @Operation(summary = "Detalhar simulado",
            description = "Retorna composicao, status de consistencia, notas relacionadas e alunos participantes calculados no backend.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Simulado encontrado",
                    content = @Content(schema = @Schema(implementation = SimuladoDetalheResponse.class))),
            @ApiResponse(responseCode = "404", description = "Simulado não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @GetMapping("/{simuladoId}")
    public ResponseEntity<SimuladoDetalheResponse> detalhar(@PathVariable Long simuladoId) {
        return ResponseEntity.ok(
                SimuladoDetalheResponse.fromApplication(detalharSimuladoUseCase.executar(simuladoId))
        );
    }

    @Operation(summary = "Listar disciplinas vinculadas ao simulado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de disciplinas (pode ser vazia)",
                    content = @Content(schema = @Schema(implementation = SimuladoDetalheResponse.DisciplinaVinculadaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Simulado não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @GetMapping("/{simuladoId}/disciplinas")
    public ResponseEntity<List<SimuladoDetalheResponse.DisciplinaVinculadaResponse>> listarDisciplinasDoSimulado(
            @PathVariable Long simuladoId
    ) {
        return ResponseEntity.ok(
                SimuladoDetalheResponse.fromApplication(detalharSimuladoUseCase.executar(simuladoId))
                        .getDisciplinas()
        );
    }

    @Operation(summary = "Editar simulado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Simulado atualizado",
                    content = @Content(schema = @Schema(implementation = SimuladoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validação ou regra de composição",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Simulado não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PatchMapping("/{simuladoId}")
    public ResponseEntity<SimuladoResponse> atualizar(
            @PathVariable Long simuladoId,
            @RequestBody @Valid AtualizarSimuladoRequest dto) {
        Simulado simulado = atualizarSimuladoUseCase.executar(
                simuladoId, dto.getDescricao(), dto.getDisciplinasIds()
        );
        return ResponseEntity.ok(new SimuladoResponse(simulado.getId(), simulado.getDescricao()));
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
                                            value = "{\"descricao\":\"Simulado Demo Swagger\",\"disciplinasIds\":[1,2]}")
                            }
                    ))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado",
                    content = @Content(schema = @Schema(implementation = SimuladoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validação ou regra de composição",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Uma ou mais disciplinas não existem",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Descrição de simulado já cadastrada",
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
