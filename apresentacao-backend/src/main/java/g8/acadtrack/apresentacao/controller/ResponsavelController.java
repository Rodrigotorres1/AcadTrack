package g8.acadtrack.apresentacao.controller;

import g8.acadtrack.aplicacao.responsavel.CriarResponsavelUseCase;
import g8.acadtrack.aplicacao.responsavel.ConsultarDesempenhoAlunoPorResponsavelUseCase;
import g8.acadtrack.aplicacao.responsavel.ConsultarNotasAlunoPorResponsavelUseCase;
import g8.acadtrack.aplicacao.responsavel.ConsultarSimuladosAlunoPorResponsavelUseCase;
import g8.acadtrack.apresentacao.dto.request.CriarResponsavelRequest;
import g8.acadtrack.apresentacao.dto.response.AnaliseDesempenhoResponse;
import g8.acadtrack.apresentacao.dto.response.ErroApiResponse;
import g8.acadtrack.apresentacao.dto.response.NotaResponse;
import g8.acadtrack.apresentacao.dto.response.ResponsavelResponse;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominiousuarios.responsavel.Responsavel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

@Tag(name = "Responsáveis", description = "Cadastro e consultas autorizadas (notas, simulados, desempenho).")
@RestController
@RequestMapping("/responsaveis")
public class ResponsavelController {

    private final CriarResponsavelUseCase criarResponsavelUseCase;
    private final ConsultarNotasAlunoPorResponsavelUseCase consultarNotasAlunoPorResponsavelUseCase;
    private final ConsultarSimuladosAlunoPorResponsavelUseCase consultarSimuladosAlunoPorResponsavelUseCase;
    private final ConsultarDesempenhoAlunoPorResponsavelUseCase consultarDesempenhoAlunoPorResponsavelUseCase;

    public ResponsavelController(
            CriarResponsavelUseCase criarResponsavelUseCase,
            ConsultarNotasAlunoPorResponsavelUseCase consultarNotasAlunoPorResponsavelUseCase,
            ConsultarSimuladosAlunoPorResponsavelUseCase consultarSimuladosAlunoPorResponsavelUseCase,
            ConsultarDesempenhoAlunoPorResponsavelUseCase consultarDesempenhoAlunoPorResponsavelUseCase
    ) {
        this.criarResponsavelUseCase = criarResponsavelUseCase;
        this.consultarNotasAlunoPorResponsavelUseCase = consultarNotasAlunoPorResponsavelUseCase;
        this.consultarSimuladosAlunoPorResponsavelUseCase = consultarSimuladosAlunoPorResponsavelUseCase;
        this.consultarDesempenhoAlunoPorResponsavelUseCase = consultarDesempenhoAlunoPorResponsavelUseCase;
    }

    @Operation(summary = "Cadastrar responsável", description = "Nome e email. Use **Examples** ou `docs/demo_fluxo_swagger_passo_a_passo.md`; se o email já existir, altere o sufixo.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CriarResponsavelRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "demo_responsavel_try_it",
                                            summary = "Responsável de demonstração",
                                            value = "{\"nome\":\"Maria Demo Swagger\",\"email\":\"swagger.responsavel.demo@local.invalid\"}"),
                                    @ExampleObject(
                                            name = "alternativa",
                                            summary = "Outro email",
                                            value = "{\"nome\":\"Responsável Demo Alt\",\"email\":\"swagger.responsavel.alt@local.invalid\"}")
                            }
                    ))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado",
                    content = @Content(schema = @Schema(implementation = ResponsavelResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validação ou regra de negócio",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ResponsavelResponse> criar(@RequestBody @Valid CriarResponsavelRequest request) {
        Responsavel responsavel = criarResponsavelUseCase.executar(
                request.getNome(),
                request.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponsavelResponse.fromDomain(responsavel));
    }

    @Operation(summary = "Listar notas do aluno (com validação responsável/aluno)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista pode ser vazia",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotaResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Sem permissão ou regra",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recursos não encontrados",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @GetMapping("/{responsavelId}/alunos/{alunoId}/notas")
    public ResponseEntity<List<NotaResponse>> consultarNotas(
            @Parameter(description = "Responsável") @PathVariable Long responsavelId,
            @Parameter(description = "Aluno") @PathVariable Long alunoId
    ) {
        List<Nota> notas = consultarNotasAlunoPorResponsavelUseCase.executar(responsavelId, alunoId);
        return ResponseEntity.ok(notas.stream().map(NotaResponse::fromDomain).toList());
    }

    @Operation(summary = "IDs de simulados em que o aluno possui nota")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista pode ser vazia",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Long.class)))),
            @ApiResponse(responseCode = "400", description = "Sem permissão ou regra",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recursos não encontrados",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @GetMapping("/{responsavelId}/alunos/{alunoId}/simulados")
    public ResponseEntity<List<Long>> consultarSimulados(
            @Parameter(description = "Responsável") @PathVariable Long responsavelId,
            @Parameter(description = "Aluno") @PathVariable Long alunoId
    ) {
        return ResponseEntity.ok(consultarSimuladosAlunoPorResponsavelUseCase.executar(responsavelId, alunoId));
    }

    @Operation(summary = "Consultar desempenho consolidado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Análise",
                    content = @Content(schema = @Schema(implementation = AnaliseDesempenhoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Sem permissão ou regra",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recursos não encontrados",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @GetMapping("/{responsavelId}/alunos/{alunoId}/desempenho")
    public ResponseEntity<AnaliseDesempenhoResponse> consultarDesempenho(
            @Parameter(description = "Responsável") @PathVariable Long responsavelId,
            @Parameter(description = "Aluno") @PathVariable Long alunoId
    ) {
        return ResponseEntity.ok(
                AnaliseDesempenhoResponse.fromApplication(
                        consultarDesempenhoAlunoPorResponsavelUseCase.executar(responsavelId, alunoId)
                )
        );
    }
}