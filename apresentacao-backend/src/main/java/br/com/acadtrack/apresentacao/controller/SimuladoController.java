package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import br.com.acadtrack.apresentacao.dto.request.CriarSimuladoRequest;
import br.com.acadtrack.apresentacao.dto.response.SimuladoResponse;
import br.com.acadtrack.dominioavaliacao.simulado.Simulado;
import jakarta.validation.Valid;
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
    public ResponseEntity<SimuladoResponse> criar(@RequestBody @Valid CriarSimuladoRequest dto) {
        Simulado simulado = criarSimuladoUseCase.executar(
                dto.getDescricao(),
                dto.getDisciplinasIds()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SimuladoResponse(simulado.getId(), simulado.getDescricao()));
    }
}