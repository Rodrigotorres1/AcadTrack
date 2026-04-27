package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.retificacao.AprovarRetificacaoNotaUseCase;
import br.com.acadtrack.aplicacao.retificacao.IniciarAnaliseRetificacaoUseCase;
import br.com.acadtrack.aplicacao.retificacao.ReprovarRetificacaoNotaUseCase;
import br.com.acadtrack.aplicacao.retificacao.SolicitarRetificacaoNotaUseCase;
import br.com.acadtrack.apresentacao.dto.AprovarRetificacaoRequest;
import br.com.acadtrack.apresentacao.dto.ReprovarRetificacaoRequest;
import br.com.acadtrack.apresentacao.dto.SolicitacaoRetificacaoResponse;
import br.com.acadtrack.apresentacao.dto.SolicitarRetificacaoRequest;
import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retificacoes")
public class RetificacaoController {

    private final SolicitarRetificacaoNotaUseCase solicitarRetificacaoNotaUseCase;
    private final IniciarAnaliseRetificacaoUseCase iniciarAnaliseRetificacaoUseCase;
    private final AprovarRetificacaoNotaUseCase aprovarRetificacaoNotaUseCase;
    private final ReprovarRetificacaoNotaUseCase reprovarRetificacaoNotaUseCase;

    public RetificacaoController(
            SolicitarRetificacaoNotaUseCase solicitarRetificacaoNotaUseCase,
            IniciarAnaliseRetificacaoUseCase iniciarAnaliseRetificacaoUseCase,
            AprovarRetificacaoNotaUseCase aprovarRetificacaoNotaUseCase,
            ReprovarRetificacaoNotaUseCase reprovarRetificacaoNotaUseCase
    ) {
        this.solicitarRetificacaoNotaUseCase = solicitarRetificacaoNotaUseCase;
        this.iniciarAnaliseRetificacaoUseCase = iniciarAnaliseRetificacaoUseCase;
        this.aprovarRetificacaoNotaUseCase = aprovarRetificacaoNotaUseCase;
        this.reprovarRetificacaoNotaUseCase = reprovarRetificacaoNotaUseCase;
    }

    @PostMapping
    public ResponseEntity<SolicitacaoRetificacaoResponse> solicitar(@RequestBody @Valid SolicitarRetificacaoRequest request) {
        SolicitacaoRetificacao solicitacao = solicitarRetificacaoNotaUseCase.executar(
                request.getNotaId(),
                request.getJustificativa()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SolicitacaoRetificacaoResponse.fromDomain(solicitacao));
    }

    @PatchMapping("/{solicitacaoId}/em-analise")
    public ResponseEntity<SolicitacaoRetificacaoResponse> iniciarAnalise(@PathVariable Long solicitacaoId) {
        SolicitacaoRetificacao solicitacao = iniciarAnaliseRetificacaoUseCase.executar(solicitacaoId);
        return ResponseEntity.ok(SolicitacaoRetificacaoResponse.fromDomain(solicitacao));
    }

    @PatchMapping("/{solicitacaoId}/aprovar")
    public ResponseEntity<SolicitacaoRetificacaoResponse> aprovar(
            @PathVariable Long solicitacaoId,
            @RequestBody @Valid AprovarRetificacaoRequest request
    ) {
        SolicitacaoRetificacao solicitacao = aprovarRetificacaoNotaUseCase.executar(
                solicitacaoId,
                request.getNovoValorNota(),
                request.getJustificativaDecisao()
        );
        return ResponseEntity.ok(SolicitacaoRetificacaoResponse.fromDomain(solicitacao));
    }

    @PatchMapping("/{solicitacaoId}/reprovar")
    public ResponseEntity<SolicitacaoRetificacaoResponse> reprovar(
            @PathVariable Long solicitacaoId,
            @RequestBody @Valid ReprovarRetificacaoRequest request
    ) {
        SolicitacaoRetificacao solicitacao = reprovarRetificacaoNotaUseCase.executar(
                solicitacaoId,
                request.getJustificativaDecisao()
        );
        return ResponseEntity.ok(SolicitacaoRetificacaoResponse.fromDomain(solicitacao));
    }
}