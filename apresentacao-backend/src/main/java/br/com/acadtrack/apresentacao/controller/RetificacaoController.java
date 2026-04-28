package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.retificacao.AprovarRetificacaoUseCase;
import br.com.acadtrack.aplicacao.retificacao.IniciarAnaliseRetificacaoUseCase;
import br.com.acadtrack.aplicacao.retificacao.ReprovarRetificacaoUseCase;
import br.com.acadtrack.aplicacao.retificacao.SolicitarRetificacaoUseCase;
import br.com.acadtrack.apresentacao.dto.request.AprovarRetificacaoRequest;
import br.com.acadtrack.apresentacao.dto.request.ReprovarRetificacaoRequest;
import br.com.acadtrack.apresentacao.dto.request.SolicitarRetificacaoRequest;
import br.com.acadtrack.apresentacao.dto.response.SolicitacaoRetificacaoResponse;
import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<SolicitacaoRetificacaoResponse> solicitar(@RequestBody @Valid SolicitarRetificacaoRequest request) {
        SolicitacaoRetificacao solicitacao = solicitarRetificacaoUseCase.executar(
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
        SolicitacaoRetificacao solicitacao = aprovarRetificacaoUseCase.executar(
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
        SolicitacaoRetificacao solicitacao = reprovarRetificacaoUseCase.executar(
                solicitacaoId,
                request.getJustificativaDecisao()
        );
        return ResponseEntity.ok(SolicitacaoRetificacaoResponse.fromDomain(solicitacao));
    }
}