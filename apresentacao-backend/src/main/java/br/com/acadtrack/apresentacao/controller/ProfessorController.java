package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.professor.CriarProfessorUseCase;
import br.com.acadtrack.apresentacao.dto.CriarProfessorRequest;
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
    public ResponseEntity<String> criar(@RequestBody CriarProfessorRequest request) {
        criarProfessorUseCase.executar(
                request.getId(),
                request.getNome(),
                request.getEmail()
        );
        return ResponseEntity.ok("Professor criado com sucesso");
    }
}