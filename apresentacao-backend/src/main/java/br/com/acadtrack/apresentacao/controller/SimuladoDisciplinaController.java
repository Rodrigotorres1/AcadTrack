package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.simulado.VincularDisciplinaSimuladoUseCase;
import br.com.acadtrack.apresentacao.dto.SimuladoDisciplinaResponse;
import br.com.acadtrack.apresentacao.dto.VincularDisciplinaSimuladoRequest;
import br.com.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulados")
public class SimuladoDisciplinaController {

    private final VincularDisciplinaSimuladoUseCase vincularDisciplinaSimuladoUseCase;

    public SimuladoDisciplinaController(VincularDisciplinaSimuladoUseCase vincularDisciplinaSimuladoUseCase) {
        this.vincularDisciplinaSimuladoUseCase = vincularDisciplinaSimuladoUseCase;
    }

    @PostMapping("/{simuladoId}/disciplinas")
    public ResponseEntity<SimuladoDisciplinaResponse> vincular(
            @PathVariable Long simuladoId,
            @RequestBody @Valid VincularDisciplinaSimuladoRequest request
    ) {
        SimuladoDisciplina resultado = vincularDisciplinaSimuladoUseCase.executar(
                simuladoId,
                request.getDisciplinaId(),
                request.getPeso()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SimuladoDisciplinaResponse.fromDomain(resultado));
    }
}
