package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.professor.CriarProfessorUseCase;
import br.com.acadtrack.apresentacao.dto.CriarProfessorRequest;
import br.com.acadtrack.apresentacao.dto.ProfessorResponse;
import br.com.acadtrack.dominiousuarios.professor.Professor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    private final CriarProfessorUseCase criarProfessorUseCase;

    public ProfessorController(CriarProfessorUseCase criarProfessorUseCase) {
        this.criarProfessorUseCase = criarProfessorUseCase;
    }

    @PostMapping
    public ResponseEntity<ProfessorResponse> criar(@RequestBody @Valid CriarProfessorRequest request) {
        Professor professor = criarProfessorUseCase.executar(
                request.getNome(),
                request.getEmail()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ProfessorResponse.fromDomain(professor));
    }
}