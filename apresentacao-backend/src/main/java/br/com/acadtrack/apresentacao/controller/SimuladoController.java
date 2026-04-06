package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import br.com.acadtrack.apresentacao.dto.CriarSimuladoRequest;
import br.com.acadtrack.apresentacao.dto.SimuladoResponse;
import br.com.acadtrack.dominioavaliacao.simulado.Simulado;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<SimuladoResponse> criar(@RequestBody CriarSimuladoRequest request) {
        Simulado simulado = criarSimuladoUseCase.executar(request.getDescricao());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SimuladoResponse.fromDomain(simulado));
    }
}