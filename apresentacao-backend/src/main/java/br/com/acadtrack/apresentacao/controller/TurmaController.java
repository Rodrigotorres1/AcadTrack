package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.turma.CriarTurmaUseCase;
import br.com.acadtrack.apresentacao.dto.CriarTurmaRequest;
import br.com.acadtrack.apresentacao.dto.TurmaResponse;
import br.com.acadtrack.dominioacademico.turma.Turma;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/turmas")
public class TurmaController {

    private final CriarTurmaUseCase criarTurmaUseCase;

    public TurmaController(CriarTurmaUseCase criarTurmaUseCase) {
        this.criarTurmaUseCase = criarTurmaUseCase;
    }

    @PostMapping
    public ResponseEntity<TurmaResponse> criar(@RequestBody @Valid CriarTurmaRequest request) {
        Turma turma = criarTurmaUseCase.executar(request.getNome());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TurmaResponse.fromDomain(turma));
    }
}