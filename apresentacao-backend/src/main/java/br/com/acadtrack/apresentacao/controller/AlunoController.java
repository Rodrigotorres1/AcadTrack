package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import br.com.acadtrack.aplicacao.turma.VincularAlunoTurmaUseCase;
import br.com.acadtrack.aplicacao.responsavel.VincularResponsavelUseCase;
import br.com.acadtrack.aplicacao.responsavel.DesvincularResponsavelUseCase;
import br.com.acadtrack.apresentacao.dto.AlunoResponse;
import br.com.acadtrack.apresentacao.dto.CriarAlunoRequest;
import br.com.acadtrack.apresentacao.dto.VincularAlunoTurmaRequest;
import br.com.acadtrack.apresentacao.dto.VincularResponsavelRequest;
import br.com.acadtrack.dominioacademico.aluno.Aluno;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final CriarAlunoUseCase criarAlunoUseCase;
    private final VincularAlunoTurmaUseCase vincularAlunoTurmaUseCase;
    private final VincularResponsavelUseCase vincularResponsavelUseCase;
    private final DesvincularResponsavelUseCase desvincularResponsavelUseCase;

    public AlunoController(
            CriarAlunoUseCase criarAlunoUseCase,
            VincularAlunoTurmaUseCase vincularAlunoTurmaUseCase,
            VincularResponsavelUseCase vincularResponsavelUseCase,
            DesvincularResponsavelUseCase desvincularResponsavelUseCase
    ) {
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.vincularAlunoTurmaUseCase = vincularAlunoTurmaUseCase;
        this.vincularResponsavelUseCase = vincularResponsavelUseCase;
        this.desvincularResponsavelUseCase = desvincularResponsavelUseCase;
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

    @PutMapping("/{alunoId}/turma")
    public ResponseEntity<AlunoResponse> vincularTurma(
            @PathVariable Long alunoId,
            @RequestBody VincularAlunoTurmaRequest request
    ) {
        Aluno aluno = vincularAlunoTurmaUseCase.executar(alunoId, request.getTurmaId());

        return ResponseEntity.ok(AlunoResponse.fromDomain(aluno));
    }

    @PutMapping("/{alunoId}/responsavel")
public ResponseEntity<AlunoResponse> vincularResponsavel(
        @PathVariable Long alunoId,
        @RequestBody VincularResponsavelRequest request
) {
    return ResponseEntity.ok(
            AlunoResponse.fromDomain(
                    vincularResponsavelUseCase.executar(alunoId, request.getResponsavelId())
            )
    );
}

@DeleteMapping("/{alunoId}/responsavel")
public ResponseEntity<AlunoResponse> desvincularResponsavel(@PathVariable Long alunoId) {
    return ResponseEntity.ok(
            AlunoResponse.fromDomain(
                    desvincularResponsavelUseCase.executar(alunoId)
            )
    );
}
}