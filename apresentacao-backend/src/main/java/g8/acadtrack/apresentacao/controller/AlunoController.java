package g8.acadtrack.apresentacao.controller;

import g8.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import g8.acadtrack.aplicacao.responsavel.DesvincularResponsavelUseCase;
import g8.acadtrack.aplicacao.responsavel.VincularResponsavelUseCase;
import g8.acadtrack.aplicacao.turma.VincularAlunoTurmaUseCase;
import g8.acadtrack.apresentacao.dto.request.CriarAlunoRequest;
import g8.acadtrack.apresentacao.dto.request.VincularAlunoTurmaRequest;
import g8.acadtrack.apresentacao.dto.request.VincularResponsavelRequest;
import g8.acadtrack.apresentacao.dto.response.AlunoResponse;
import g8.acadtrack.apresentacao.dto.response.ErroApiResponse;
import g8.acadtrack.dominioacademico.aluno.Aluno;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Alunos", description = "Cadastro, vínculos com turma e com responsável.")
@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final CriarAlunoUseCase criarAlunoUseCase;
    private final VincularAlunoTurmaUseCase vincularAlunoTurmaUseCase;
    private final VincularResponsavelUseCase vincularResponsavelUseCase;
    private final DesvincularResponsavelUseCase desvincularResponsavelUseCase;

    public AlunoController(
            CriarAlunoUseCase criarAlunoUseCase,
            VincularAlunoTurmaUseCase vincularAlunoTurmaUseCase,
            VincularResponsavelUseCase vincularResponsavelUseCase,
            DesvincularResponsavelUseCase desvincularResponsavelUseCase
    ) {
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.vincularAlunoTurmaUseCase = vincularAlunoTurmaUseCase;
        this.vincularResponsavelUseCase = vincularResponsavelUseCase;
        this.desvincularResponsavelUseCase = desvincularResponsavelUseCase;
    }

    @Operation(
            summary = "Cadastrar aluno",
            description = "Somente `nome` e `email` neste POST. E-mail **único** no sistema (mesmo endereço ignorando maiúsculas). No request body, use **Examples** ou `docs/demo_fluxo_swagger_passo_a_passo.md`.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CriarAlunoRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "demo_try_it_pronto",
                                            summary = "Nome e e-mail prontos (ajuste o e-mail em caso de duplicado)",
                                            value = "{\"nome\":\"Aluno Demo Swagger\",\"email\":\"swagger.aluno.demo@local.invalid\"}"),
                                    @ExampleObject(
                                            name = "segundo_exemplo",
                                            summary = "Mesma forma, outro e-mail",
                                            value = "{\"nome\":\"Aluno Demo 2\",\"email\":\"swagger.aluno.alt2@local.invalid\"}")
                            }
                    ))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado",
                    content = @Content(schema = @Schema(implementation = AlunoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validação ou regra de negócio",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado para outro aluno",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PostMapping
    public ResponseEntity<AlunoResponse> criar(@RequestBody @Valid CriarAlunoRequest request) {
        Aluno aluno = criarAlunoUseCase.executar(request.getNome(), request.getEmail());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AlunoResponse.fromDomain(aluno));
    }

    @Operation(summary = "Definir ou trocar turma do aluno",
            description = "`alunoId` no URL deve ser o `id` da resposta 201 do **POST /alunos**. `turmaId` vem do **POST /turmas**.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = VincularAlunoTurmaRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "demo_turma_id",
                                            summary = "Substitua turmaId pelo id retornado em POST /turmas",
                                            value = "{\"turmaId\":1}")
                            }
                    ))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Confirmação com aluno atualizado",
                    content = @Content(schema = @Schema(implementation = AlunoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validação ou regra de negócio",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Aluno ou turma não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PutMapping("/{alunoId}/turma")
    public ResponseEntity<AlunoResponse> vincularTurma(
            @Parameter(description = "Aluno") @PathVariable Long alunoId,
            @RequestBody @Valid VincularAlunoTurmaRequest request
    ) {
        Aluno aluno = vincularAlunoTurmaUseCase.executar(alunoId, request.getTurmaId());
        return ResponseEntity.ok(AlunoResponse.fromDomain(aluno));
    }

    @Operation(summary = "Vincular responsável ao aluno",
            description = "`responsavelId` deve ser o `id` da resposta **201** do **POST /responsaveis**. Precisa conceder pelo menos uma permissão `true`.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = VincularResponsavelRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "demo_responsavel_todas_permissoes",
                                            summary = "Substitua responsavelId pelo id criado antes",
                                            value = "{\"responsavelId\":1,\"podeVisualizarNotas\":true,\"podeVisualizarSimulados\":true,\"podeVisualizarDesempenho\":true}")
                            }
                    ))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Confirmação com aluno atualizado",
                    content = @Content(schema = @Schema(implementation = AlunoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validação ou regra de negócio",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recursos não encontrados",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Estado conflitante",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PutMapping("/{alunoId}/responsavel")
    public ResponseEntity<AlunoResponse> vincularResponsavel(
            @Parameter(description = "Aluno") @PathVariable Long alunoId,
            @RequestBody @Valid VincularResponsavelRequest request
    ) {
        Aluno aluno = vincularResponsavelUseCase.executar(
                alunoId,
                request.getResponsavelId(),
                request.isPodeVisualizarNotas(),
                request.isPodeVisualizarSimulados(),
                request.isPodeVisualizarDesempenho()
        );
        return ResponseEntity.ok(AlunoResponse.fromDomain(aluno));
    }

    @Operation(summary = "Desvincular responsável do aluno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Confirmação com aluno atualizado",
                    content = @Content(schema = @Schema(implementation = AlunoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Aluno ou vínculo não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @DeleteMapping("/{alunoId}/responsavel")
    public ResponseEntity<AlunoResponse> desvincularResponsavel(
            @Parameter(description = "Aluno") @PathVariable Long alunoId) {
        Aluno aluno = desvincularResponsavelUseCase.executar(alunoId);
        return ResponseEntity.ok(AlunoResponse.fromDomain(aluno));
    }
}
