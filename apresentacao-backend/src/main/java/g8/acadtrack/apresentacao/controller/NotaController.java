package g8.acadtrack.apresentacao.controller;

import g8.acadtrack.aplicacao.nota.BuscarNotasPorAlunoUseCase;
import g8.acadtrack.aplicacao.nota.CalcularMediaPonderadaUseCase;
import g8.acadtrack.aplicacao.nota.LancarNotaUseCase;
import g8.acadtrack.aplicacao.nota.RankingAlunosUseCase;
import g8.acadtrack.apresentacao.dto.request.LancarNotaRequest;
import g8.acadtrack.apresentacao.dto.response.ErroApiResponse;
import g8.acadtrack.apresentacao.dto.response.NotaResponse;
import g8.acadtrack.dominioavaliacao.nota.Nota;
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
import java.util.Map;

@Tag(name = "Notas",
        description = "Lançamento e consultas. Muitos fluxos assumem dados já criados (aluno, simulado, vínculos) — "
                + "ver `docs/script_demonstracao.md`. Os GET /notas/ranking legados não recebem id de aluno no URL.")
@RestController
@RequestMapping("/notas")
public class NotaController {

    private final LancarNotaUseCase lancarNotaUseCase;
    private final BuscarNotasPorAlunoUseCase buscarNotasPorAlunoUseCase;
    private final CalcularMediaPonderadaUseCase calcularMediaPonderadaUseCase;
    private final RankingAlunosUseCase rankingAlunosUseCase;

    public NotaController(LancarNotaUseCase lancarNotaUseCase,
                          BuscarNotasPorAlunoUseCase buscarNotasPorAlunoUseCase,
                          CalcularMediaPonderadaUseCase calcularMediaPonderadaUseCase,
                          RankingAlunosUseCase rankingAlunosUseCase) {
        this.lancarNotaUseCase = lancarNotaUseCase;
        this.buscarNotasPorAlunoUseCase = buscarNotasPorAlunoUseCase;
        this.calcularMediaPonderadaUseCase = calcularMediaPonderadaUseCase;
        this.rankingAlunosUseCase = rankingAlunosUseCase;
    }

    @Operation(summary = "Lançar nota",
            description = "Substitua `alunoId`, `simuladoId` e `disciplinaId` pelos **id** devolvidos em `POST /alunos`, `POST /simulados`, `POST /disciplinas`. Nos **Examples** abaixo, `1` só funciona na base inicial vazia; caso contrário, edite antes de executar.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LancarNotaRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "demo_primeira_disciplina",
                                            summary = "IDs de exemplo — alinhar com suas respostas 201",
                                            value = "{\"alunoId\":1,\"simuladoId\":1,\"disciplinaId\":1,\"valor\":7.5}"),
                                    @ExampleObject(
                                            name = "demo_segunda_disciplina",
                                            summary = "Troca só disciplinaId (ex. nota na outra matéria)",
                                            value = "{\"alunoId\":1,\"simuladoId\":1,\"disciplinaId\":2,\"valor\":8.0}")
                            }
                    ))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Persistida",
                    content = @Content(schema = @Schema(implementation = NotaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validação ou regra de negócio",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PostMapping
    public ResponseEntity<NotaResponse> criar(@RequestBody @Valid LancarNotaRequest request) {
        Nota nota = lancarNotaUseCase.executar(
                request.getAlunoId(),
                request.getSimuladoId(),
                request.getDisciplinaId(),
                request.getValor()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(NotaResponse.fromDomain(nota));
    }

    @Operation(summary = "Listar notas do aluno", description = "Informe alunoId no caminho usando o campo id devolvido no corpo ao criar com POST /alunos. Valores apenas de exemplo como 1 so funcionam se existir esse registo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista pode ser vazia",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotaResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<NotaResponse>> buscarPorAluno(
            @Parameter(description = "`id` do aluno criado via POST /alunos (campo id no JSON 201)", example = "1") @PathVariable Long alunoId) {
        List<NotaResponse> response = buscarNotasPorAlunoUseCase.executar(alunoId)
                .stream()
                .map(NotaResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Média ponderada do aluno no simulado",
            description = "Ambos os IDs devem corresponder a recursos existentes criados antes (POST); sem notas no simulado pode dar 400.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valor numérico (JSON number)"),
            @ApiResponse(responseCode = "400", description = "Sem notas suficientes ou regra",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Aluno/simulado não encontrado ou sem dados",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @GetMapping("/aluno/{alunoId}/simulado/{simuladoId}/media")
    public ResponseEntity<Double> calcularMediaPonderada(
            @Parameter(description = "`id` do aluno devolvido pelo POST /alunos", example = "1") @PathVariable Long alunoId,
            @Parameter(description = "`id` do simulado devolvido pelo POST /simulados", example = "1") @PathVariable Long simuladoId) {
        double media = calcularMediaPonderadaUseCase.executar(alunoId, simuladoId);
        return ResponseEntity.ok(media);
    }

    @Deprecated(since = "1.0", forRemoval = false)
    @Operation(summary = "[Legado] ranking em mapas", deprecated = true,
            description = """
                    Não há query nem path com id de aluno; é um ranking agregado em mapas.
                    Com base vazia de notas, a lista pode vir vazia. Preferir GET /rankings/{simuladoId}.""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista heterogênea (map)")
    })
    @GetMapping("/ranking")
    public ResponseEntity<List<Map<String, Object>>> ranking() {
        return ResponseEntity.ok(rankingAlunosUseCase.executar());
    }

    @Deprecated(since = "1.0", forRemoval = false)
    @Operation(summary = "[Legado] primeiro colocado como mapa único", deprecated = true,
            description = """
                    Também sem parâmetros de aluno/simulado no URL.
                    Se ninguém no ranking → HTTP 204. Preferir GET /rankings/{simuladoId}.""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Map com dados do topo"),
            @ApiResponse(responseCode = "204", description = "Ranking vazio")
    })
    @GetMapping("/ranking/top")
    public ResponseEntity<Map<String, Object>> top() {
        List<Map<String, Object>> ranking = rankingAlunosUseCase.executar();
        if (ranking.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ranking.get(0));
    }
}
