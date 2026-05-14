package g8.acadtrack.apresentacao.controller;

import g8.acadtrack.aplicacao.disciplina.AtualizarDisciplinaUseCase;
import g8.acadtrack.aplicacao.disciplina.BuscarDisciplinaUseCase;
import g8.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import g8.acadtrack.aplicacao.disciplina.ExcluirDisciplinaUseCase;
import g8.acadtrack.aplicacao.disciplina.InativarDisciplinaUseCase;
import g8.acadtrack.aplicacao.disciplina.AtivarDisciplinaUseCase;
import g8.acadtrack.aplicacao.disciplina.ListarDisciplinasUseCase;
import g8.acadtrack.apresentacao.dto.request.AtualizarDisciplinaRequest;
import g8.acadtrack.apresentacao.dto.request.CriarDisciplinaRequest;
import g8.acadtrack.apresentacao.dto.response.DisciplinaResponse;
import g8.acadtrack.apresentacao.dto.response.ErroApiResponse;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Disciplinas", description = "Cadastro, inativação e reativação de disciplinas.")
@RestController
@RequestMapping("/disciplinas")
public class DisciplinaController {

    private final CriarDisciplinaUseCase useCase;
    private final AtualizarDisciplinaUseCase atualizarDisciplinaUseCase;
    private final ListarDisciplinasUseCase listarDisciplinasUseCase;
    private final BuscarDisciplinaUseCase buscarDisciplinaUseCase;
    private final InativarDisciplinaUseCase inativarDisciplinaUseCase;
    private final AtivarDisciplinaUseCase ativarDisciplinaUseCase;
    private final ExcluirDisciplinaUseCase excluirDisciplinaUseCase;

    public DisciplinaController(
            CriarDisciplinaUseCase useCase,
            AtualizarDisciplinaUseCase atualizarDisciplinaUseCase,
            ListarDisciplinasUseCase listarDisciplinasUseCase,
            BuscarDisciplinaUseCase buscarDisciplinaUseCase,
            InativarDisciplinaUseCase inativarDisciplinaUseCase,
            AtivarDisciplinaUseCase ativarDisciplinaUseCase,
            ExcluirDisciplinaUseCase excluirDisciplinaUseCase
    ) {
        this.useCase = useCase;
        this.atualizarDisciplinaUseCase = atualizarDisciplinaUseCase;
        this.listarDisciplinasUseCase = listarDisciplinasUseCase;
        this.buscarDisciplinaUseCase = buscarDisciplinaUseCase;
        this.inativarDisciplinaUseCase = inativarDisciplinaUseCase;
        this.ativarDisciplinaUseCase = ativarDisciplinaUseCase;
        this.excluirDisciplinaUseCase = excluirDisciplinaUseCase;
    }

    @Operation(summary = "Listar disciplinas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista pode ser vazia")
    })
    @GetMapping
    public ResponseEntity<List<DisciplinaResponse>> listar() {
        return ResponseEntity.ok(
                listarDisciplinasUseCase.executar()
                        .stream()
                        .map(DisciplinaResponse::fromDomain)
                        .toList()
        );
    }

    @Operation(summary = "Buscar detalhes da disciplina")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplina encontrada",
                    content = @Content(schema = @Schema(implementation = DisciplinaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @GetMapping("/{disciplinaId}")
    public ResponseEntity<DisciplinaResponse> buscarPorId(
            @Parameter(description = "Identificador da disciplina") @PathVariable Long disciplinaId) {
        Disciplina disciplina = buscarDisciplinaUseCase.executar(disciplinaId);
        return ResponseEntity.ok(DisciplinaResponse.fromDomain(disciplina));
    }

    @Operation(summary = "Cadastrar disciplina", description = "O id é gerado pelo servidor; envie apenas o nome. Dropdown **Examples** para corpo pronto.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CriarDisciplinaRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "demo_disciplina",
                                            summary = "Só campo nome",
                                            value = "{\"nome\":\"Matematica Demo Fluxo Swagger\"}")
                            }
                    ))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criada",
                    content = @Content(schema = @Schema(implementation = DisciplinaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados de entrada",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de estado: nome de disciplina já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PostMapping
    public ResponseEntity<DisciplinaResponse> criar(@RequestBody @Valid CriarDisciplinaRequest request) {
        Disciplina disciplina = useCase.executar(request.nome());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(DisciplinaResponse.fromDomain(disciplina));
    }

    @Operation(summary = "Editar nome da disciplina")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplina atualizada",
                    content = @Content(schema = @Schema(implementation = DisciplinaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados de entrada",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de estado: nome de disciplina já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PatchMapping("/{disciplinaId}")
    public ResponseEntity<DisciplinaResponse> atualizar(
            @Parameter(description = "Identificador da disciplina") @PathVariable Long disciplinaId,
            @RequestBody @Valid AtualizarDisciplinaRequest request) {
        Disciplina disciplina = atualizarDisciplinaUseCase.executar(disciplinaId, request.getNome());
        return ResponseEntity.ok(DisciplinaResponse.fromDomain(disciplina));
    }

    @Operation(summary = "Inativar disciplina (soft-delete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado atualizado",
                    content = @Content(schema = @Schema(implementation = DisciplinaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Regra de negócio",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PatchMapping("/{disciplinaId}/inativar")
    public ResponseEntity<DisciplinaResponse> inativar(
            @Parameter(description = "Identificador da disciplina") @PathVariable Long disciplinaId) {
        Disciplina disciplina = inativarDisciplinaUseCase.executar(disciplinaId);
        return ResponseEntity.ok(DisciplinaResponse.fromDomain(disciplina));
    }

    @Operation(summary = "Reativar disciplina")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado atualizado",
                    content = @Content(schema = @Schema(implementation = DisciplinaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Regra de negócio",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PatchMapping("/{disciplinaId}/ativar")
    public ResponseEntity<DisciplinaResponse> ativar(
            @Parameter(description = "Identificador da disciplina") @PathVariable Long disciplinaId) {
        Disciplina disciplina = ativarDisciplinaUseCase.executar(disciplinaId);
        return ResponseEntity.ok(DisciplinaResponse.fromDomain(disciplina));
    }

    @Operation(summary = "Excluir disciplina definitivamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Disciplina excluída"),
            @ApiResponse(responseCode = "400", description = "Regra de negócio",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @DeleteMapping("/{disciplinaId}")
    public ResponseEntity<Void> excluir(
            @Parameter(description = "Identificador da disciplina") @PathVariable Long disciplinaId) {
        excluirDisciplinaUseCase.executar(disciplinaId);
        return ResponseEntity.noContent().build();
    }
}
