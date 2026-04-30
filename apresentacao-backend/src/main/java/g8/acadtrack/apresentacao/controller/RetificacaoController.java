package g8.acadtrack.apresentacao.controller;

import g8.acadtrack.aplicacao.retificacao.AprovarRetificacaoUseCase;
import g8.acadtrack.aplicacao.retificacao.IniciarAnaliseRetificacaoUseCase;
import g8.acadtrack.aplicacao.retificacao.ReprovarRetificacaoUseCase;
import g8.acadtrack.aplicacao.retificacao.SolicitarRetificacaoUseCase;
import g8.acadtrack.apresentacao.dto.request.AprovarRetificacaoRequest;
import g8.acadtrack.apresentacao.dto.request.ReprovarRetificacaoRequest;
import g8.acadtrack.apresentacao.dto.request.SolicitarRetificacaoRequest;
import g8.acadtrack.apresentacao.dto.response.ErroApiResponse;
import g8.acadtrack.apresentacao.dto.response.SolicitacaoRetificacaoResponse;
import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "Retificações", description = "Workflow de solicitação, análise, aprovação e reprovação de notas.")
@RestController
@RequestMapping("/retificacoes")
public class RetificacaoController {

    private final SolicitarRetificacaoUseCase solicitarRetificacaoUseCase;
    private final IniciarAnaliseRetificacaoUseCase iniciarAnaliseRetificacaoUseCase;
    private final AprovarRetificacaoUseCase aprovarRetificacaoUseCase;
    private final ReprovarRetificacaoUseCase reprovarRetificacaoUseCase;

    public RetificacaoController(
            SolicitarRetificacaoUseCase solicitarRetificacaoUseCase,
            IniciarAnaliseRetificacaoUseCase iniciarAnaliseRetificacaoUseCase,
            AprovarRetificacaoUseCase aprovarRetificacaoUseCase,
            ReprovarRetificacaoUseCase reprovarRetificacaoUseCase
    ) {
        this.solicitarRetificacaoUseCase = solicitarRetificacaoUseCase;
        this.iniciarAnaliseRetificacaoUseCase = iniciarAnaliseRetificacaoUseCase;
        this.aprovarRetificacaoUseCase = aprovarRetificacaoUseCase;
        this.reprovarRetificacaoUseCase = reprovarRetificacaoUseCase;
    }

    @Operation(summary = "Registrar solicitação de retificação de nota")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitação criada",
                    content = @Content(schema = @Schema(implementation = SolicitacaoRetificacaoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validação ou regra de negócio",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PostMapping
    public ResponseEntity<SolicitacaoRetificacaoResponse> solicitar(@RequestBody @Valid SolicitarRetificacaoRequest request) {
        SolicitacaoRetificacao solicitacao = solicitarRetificacaoUseCase.executar(
                request.getNotaId(),
                request.getJustificativa()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SolicitacaoRetificacaoResponse.fromDomain(solicitacao));
    }

    @Operation(summary = "Colocar solicitação em análise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado atualizado",
                    content = @Content(schema = @Schema(implementation = SolicitacaoRetificacaoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Transição não permitida",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "Regra",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PatchMapping("/{solicitacaoId}/em-analise")
    public ResponseEntity<SolicitacaoRetificacaoResponse> iniciarAnalise(
            @Parameter(description = "Identificador da solicitação") @PathVariable Long solicitacaoId) {
        SolicitacaoRetificacao solicitacao = iniciarAnaliseRetificacaoUseCase.executar(solicitacaoId);
        return ResponseEntity.ok(SolicitacaoRetificacaoResponse.fromDomain(solicitacao));
    }

    @Operation(summary = "Aprovar retificação (novo valor e justificativa de decisão)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Decisão persistida",
                    content = @Content(schema = @Schema(implementation = SolicitacaoRetificacaoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validação ou regra",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Estado inadequado para aprovação",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PatchMapping("/{solicitacaoId}/aprovar")
    public ResponseEntity<SolicitacaoRetificacaoResponse> aprovar(
            @Parameter(description = "Solicitação") @PathVariable Long solicitacaoId,
            @RequestBody @Valid AprovarRetificacaoRequest request
    ) {
        SolicitacaoRetificacao solicitacao = aprovarRetificacaoUseCase.executar(
                solicitacaoId,
                request.getNovoValorNota(),
                request.getJustificativaDecisao()
        );
        return ResponseEntity.ok(SolicitacaoRetificacaoResponse.fromDomain(solicitacao));
    }

    @Operation(summary = "Reprovar retificação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Decisão persistida",
                    content = @Content(schema = @Schema(implementation = SolicitacaoRetificacaoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validação ou regra",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Estado inadequado",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PatchMapping("/{solicitacaoId}/reprovar")
    public ResponseEntity<SolicitacaoRetificacaoResponse> reprovar(
            @Parameter(description = "Solicitação") @PathVariable Long solicitacaoId,
            @RequestBody @Valid ReprovarRetificacaoRequest request
    ) {
        SolicitacaoRetificacao solicitacao = reprovarRetificacaoUseCase.executar(
                solicitacaoId,
                request.getJustificativaDecisao()
        );
        return ResponseEntity.ok(SolicitacaoRetificacaoResponse.fromDomain(solicitacao));
    }
}