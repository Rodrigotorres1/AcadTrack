package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import br.com.acadtrack.apresentacao.dto.CriarSimuladoRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simulados")
public class SimuladoController {

    private final CriarSimuladoUseCase criarSimuladoUseCase;

    public SimuladoController(CriarSimuladoUseCase criarSimuladoUseCase) {
        this.criarSimuladoUseCase = criarSimuladoUseCase;
    }

    @PostMapping
    public ResponseEntity<String> criar(@RequestBody CriarSimuladoRequest request) {
        criarSimuladoUseCase.executar(request.getId(), request.getDescricao());
        return ResponseEntity.ok("Simulado criado com sucesso");
    }
}