package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.retificacao.SolicitarRetificacaoNotaUseCase;
import br.com.acadtrack.apresentacao.dto.SolicitarRetificacaoRequest;
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
    public ResponseEntity<String> solicitar(@RequestBody SolicitarRetificacaoRequest request) {
        solicitarRetificacaoNotaUseCase.executar(
                request.getId(),
                request.getNotaId(),
                request.getJustificativa()
        );
        return ResponseEntity.ok("Solicitação de retificação registrada com sucesso");
    }
}