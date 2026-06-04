package g8.acadtrack.apresentacao.controller;

import g8.acadtrack.aplicacao.turma.CriarTurmaUseCase;
import g8.acadtrack.aplicacao.turma.ExcluirTurmaUseCase;
import g8.acadtrack.aplicacao.turma.ListarTurmasUseCase;
import g8.acadtrack.apresentacao.dto.request.CriarTurmaRequest;
import g8.acadtrack.apresentacao.dto.response.ErroApiResponse;
import g8.acadtrack.apresentacao.dto.response.TurmaResponse;
import g8.acadtrack.dominioacademico.turma.Turma;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(name = "Turmas", description = "Cadastro de turmas.")
@RestController
@RequestMapping("/turmas")
public class TurmaController {

    private final CriarTurmaUseCase criarTurmaUseCase;
    private final ListarTurmasUseCase listarTurmasUseCase;
    private final ExcluirTurmaUseCase excluirTurmaUseCase;

    public TurmaController(CriarTurmaUseCase criarTurmaUseCase,
                           ListarTurmasUseCase listarTurmasUseCase,
                           ExcluirTurmaUseCase excluirTurmaUseCase) {
        this.criarTurmaUseCase = criarTurmaUseCase;
        this.listarTurmasUseCase = listarTurmasUseCase;
        this.excluirTurmaUseCase = excluirTurmaUseCase;
    }

    @Operation(summary = "Listar turmas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista pode ser vazia")
    })
    @GetMapping
    public ResponseEntity<List<TurmaResponse>> listar() {
        return ResponseEntity.ok(
                listarTurmasUseCase.executar()
                        .stream()
                        .map(TurmaResponse::fromDomain)
                        .toList()
        );
    }

    @Operation(
            summary = "Cadastrar turma",
            description = "Um nome basta. Veja `docs/demo_fluxo_swagger_passo_a_passo.md` para encaixar no fluxo.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CriarTurmaRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "demo_turma",
                                            summary = "Nome de turma pronto",
                                            value = "{\"nome\":\"Turma Demo Swagger\"}")
                            }
                    ))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criada",
                    content = @Content(schema = @Schema(implementation = TurmaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validação ou regra de negócio",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Nome de turma já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PostMapping
    public ResponseEntity<TurmaResponse> criar(@RequestBody @Valid CriarTurmaRequest request) {
        Turma turma = criarTurmaUseCase.executar(request.getNome());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TurmaResponse.fromDomain(turma));
    }

    @Operation(summary = "Excluir turma", description = "Remove a turma se não houver alunos matriculados nela.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Turma excluída"),
            @ApiResponse(responseCode = "404", description = "Turma não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Turma possui alunos matriculados",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @DeleteMapping("/{turmaId}")
    public ResponseEntity<Void> excluir(
            @Parameter(description = "Identificador da turma") @PathVariable Long turmaId) {
        excluirTurmaUseCase.executar(turmaId);
        return ResponseEntity.noContent().build();
    }
}
