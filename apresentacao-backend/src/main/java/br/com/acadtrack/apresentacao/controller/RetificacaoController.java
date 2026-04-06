package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.retificacao.SolicitarRetificacaoNotaUseCase;
import br.com.acadtrack.apresentacao.dto.SolicitacaoRetificacaoResponse;
import br.com.acadtrack.apresentacao.dto.SolicitarRetificacaoRequest;
import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retificacoes")
public class RetificacaoController {

    private final SolicitarRetificacaoNotaUseCase solicitarRetificacaoNotaUseCase;

    public RetificacaoController(SolicitarRetificacaoNotaUseCase solicitarRetificacaoNotaUseCase) {
        this.solicitarRetificacaoNotaUseCase = solicitarRetificacaoNotaUseCase;
    }

    @PostMapping
    public ResponseEntity<SolicitacaoRetificacaoResponse> solicitar(@RequestBody SolicitarRetificacaoRequest request) {
        SolicitacaoRetificacao solicitacao = solicitarRetificacaoNotaUseCase.executar(
                request.getNotaId(),
                request.getJustificativa()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SolicitacaoRetificacaoResponse.fromDomain(solicitacao));
    }
}