package g8.acadtrack.apresentacao.controller;

import g8.acadtrack.aplicacao.notificacao.ListarNotificacoesResponsavelUseCase;
import g8.acadtrack.aplicacao.notificacao.MarcarNotificacaoLidaUseCase;
import g8.acadtrack.aplicacao.responsavel.CriarResponsavelUseCase;
import g8.acadtrack.aplicacao.responsavel.ExcluirResponsavelUseCase;
import g8.acadtrack.aplicacao.responsavel.ListarResponsaveisUseCase;
import g8.acadtrack.aplicacao.responsavel.ConsultarDesempenhoAlunoPorResponsavelUseCase;
import g8.acadtrack.aplicacao.responsavel.ConsultarNotasAlunoPorResponsavelUseCase;
import g8.acadtrack.aplicacao.responsavel.ConsultarSimuladosAlunoPorResponsavelUseCase;
import g8.acadtrack.apresentacao.dto.request.CriarResponsavelRequest;
import g8.acadtrack.apresentacao.dto.response.AnaliseDesempenhoResponse;
import g8.acadtrack.apresentacao.dto.response.ErroApiResponse;
import g8.acadtrack.apresentacao.dto.response.NotificacaoResponsavelResponse;
import g8.acadtrack.apresentacao.dto.response.NotaResponse;
import g8.acadtrack.apresentacao.dto.response.ResponsavelResponse;
import g8.acadtrack.apresentacao.dto.response.SimuladoResponse;
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
    private final ExcluirResponsavelUseCase excluirResponsavelUseCase;
    private final ListarResponsaveisUseCase listarResponsaveisUseCase;
    private final ConsultarNotasAlunoPorResponsavelUseCase consultarNotasAlunoPorResponsavelUseCase;
    private final ConsultarSimuladosAlunoPorResponsavelUseCase consultarSimuladosAlunoPorResponsavelUseCase;
    private final ConsultarDesempenhoAlunoPorResponsavelUseCase consultarDesempenhoAlunoPorResponsavelUseCase;
    private final ListarNotificacoesResponsavelUseCase listarNotificacoesResponsavelUseCase;
    private final MarcarNotificacaoLidaUseCase marcarNotificacaoLidaUseCase;

    public ResponsavelController(
            CriarResponsavelUseCase criarResponsavelUseCase,
            ExcluirResponsavelUseCase excluirResponsavelUseCase,
            ListarResponsaveisUseCase listarResponsaveisUseCase,
            ConsultarNotasAlunoPorResponsavelUseCase consultarNotasAlunoPorResponsavelUseCase,
            ConsultarSimuladosAlunoPorResponsavelUseCase consultarSimuladosAlunoPorResponsavelUseCase,
            ConsultarDesempenhoAlunoPorResponsavelUseCase consultarDesempenhoAlunoPorResponsavelUseCase,
            ListarNotificacoesResponsavelUseCase listarNotificacoesResponsavelUseCase,
            MarcarNotificacaoLidaUseCase marcarNotificacaoLidaUseCase
    ) {
        this.criarResponsavelUseCase = criarResponsavelUseCase;
        this.excluirResponsavelUseCase = excluirResponsavelUseCase;
        this.listarResponsaveisUseCase = listarResponsaveisUseCase;
        this.consultarNotasAlunoPorResponsavelUseCase = consultarNotasAlunoPorResponsavelUseCase;
        this.consultarSimuladosAlunoPorResponsavelUseCase = consultarSimuladosAlunoPorResponsavelUseCase;
        this.consultarDesempenhoAlunoPorResponsavelUseCase = consultarDesempenhoAlunoPorResponsavelUseCase;
        this.listarNotificacoesResponsavelUseCase = listarNotificacoesResponsavelUseCase;
        this.marcarNotificacaoLidaUseCase = marcarNotificacaoLidaUseCase;
    }

    @Operation(summary = "Listar responsáveis cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista pode ser vazia",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponsavelResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<ResponsavelResponse>> listar() {
        return ResponseEntity.ok(
                listarResponsaveisUseCase.executar()
                        .stream()
                        .map(ResponsavelResponse::fromDomain)
                        .toList()
        );
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
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados de entrada",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de estado: e-mail de responsável já cadastrado",
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

    @Operation(summary = "Excluir responsável definitivamente", description = "Remove o cadastro do responsável e limpa vínculos existentes com alunos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Responsável excluído"),
            @ApiResponse(responseCode = "404", description = "Responsável não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @DeleteMapping("/{responsavelId}")
    public ResponseEntity<Void> excluir(
            @Parameter(description = "Identificador do responsavel") @PathVariable Long responsavelId) {
        excluirResponsavelUseCase.executar(responsavelId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar notas do aluno (com validação responsável/aluno)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista pode ser vazia",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotaResponse.class)))),
            @ApiResponse(responseCode = "403", description = "Responsável sem vínculo ativo ou sem permissão",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Aluno ou responsável não encontrado",
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

    @Operation(summary = "Listar simulados em que o aluno possui nota")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista pode ser vazia",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SimuladoResponse.class)))),
            @ApiResponse(responseCode = "403", description = "Responsável sem vínculo ativo ou sem permissão",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recursos não encontrados",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @GetMapping("/{responsavelId}/alunos/{alunoId}/simulados")
    public ResponseEntity<List<SimuladoResponse>> consultarSimulados(
            @Parameter(description = "Responsável") @PathVariable Long responsavelId,
            @Parameter(description = "Aluno") @PathVariable Long alunoId
    ) {
        return ResponseEntity.ok(
                consultarSimuladosAlunoPorResponsavelUseCase.executar(responsavelId, alunoId)
                        .stream()
                        .map(SimuladoResponse::fromDomain)
                        .toList()
        );
    }

    @Operation(summary = "Consultar desempenho consolidado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Análise",
                    content = @Content(schema = @Schema(implementation = AnaliseDesempenhoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Regra de negócio",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Responsável sem vínculo ativo ou sem permissão",
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

    @Operation(summary = "Listar notificacoes do responsavel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista pode ser vazia",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificacaoResponsavelResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Responsável não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @GetMapping("/{responsavelId}/notificacoes")
    public ResponseEntity<List<NotificacaoResponsavelResponse>> listarNotificacoes(
            @Parameter(description = "Responsável") @PathVariable Long responsavelId
    ) {
        return ResponseEntity.ok(
                listarNotificacoesResponsavelUseCase.executar(responsavelId)
                        .stream()
                        .map(NotificacaoResponsavelResponse::fromDomain)
                        .toList()
        );
    }

    @Operation(summary = "Marcar notificação como lida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificação atualizada",
                    content = @Content(schema = @Schema(implementation = NotificacaoResponsavelResponse.class))),
            @ApiResponse(responseCode = "404", description = "Responsável ou notificação não encontrados",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PatchMapping("/{responsavelId}/notificacoes/{notificacaoId}/lida")
    public ResponseEntity<NotificacaoResponsavelResponse> marcarNotificacaoLida(
            @Parameter(description = "Responsável") @PathVariable Long responsavelId,
            @Parameter(description = "Notificação") @PathVariable Long notificacaoId
    ) {
        return ResponseEntity.ok(
                NotificacaoResponsavelResponse.fromDomain(
                        marcarNotificacaoLidaUseCase.executar(responsavelId, notificacaoId)
                )
        );
    }
}
