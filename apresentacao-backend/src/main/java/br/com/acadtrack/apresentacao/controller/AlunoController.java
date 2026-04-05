package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import br.com.acadtrack.apresentacao.dto.AlunoResponse;
import br.com.acadtrack.apresentacao.dto.CriarAlunoRequest;
import br.com.acadtrack.dominioacademico.aluno.Aluno;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final CriarAlunoUseCase criarAlunoUseCase;

    public AlunoController(CriarAlunoUseCase criarAlunoUseCase) {
        this.criarAlunoUseCase = criarAlunoUseCase;
    }

    @PostMapping
    public ResponseEntity<AlunoResponse> criar(@RequestBody CriarAlunoRequest request) {
        Aluno aluno = criarAlunoUseCase.executar(
                request.getNome(),
                request.getEmail()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AlunoResponse.fromDomain(aluno));
    }
}