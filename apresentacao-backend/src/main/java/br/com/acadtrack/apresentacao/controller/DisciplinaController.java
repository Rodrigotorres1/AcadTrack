package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import br.com.acadtrack.aplicacao.disciplina.InativarDisciplinaUseCase;
import br.com.acadtrack.aplicacao.disciplina.AtivarDisciplinaUseCase;
import br.com.acadtrack.apresentacao.dto.CriarDisciplinaRequest;
import br.com.acadtrack.apresentacao.dto.DisciplinaResponse;
import br.com.acadtrack.dominioacademico.disciplina.Disciplina;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/disciplinas")
public class DisciplinaController {

    private final CriarDisciplinaUseCase useCase;
    private final InativarDisciplinaUseCase inativarDisciplinaUseCase;
    private final AtivarDisciplinaUseCase ativarDisciplinaUseCase;

    public DisciplinaController(
            CriarDisciplinaUseCase useCase,
            InativarDisciplinaUseCase inativarDisciplinaUseCase,
            AtivarDisciplinaUseCase ativarDisciplinaUseCase
    ) {
        this.useCase = useCase;
        this.inativarDisciplinaUseCase = inativarDisciplinaUseCase;
        this.ativarDisciplinaUseCase = ativarDisciplinaUseCase;
    }

    @PostMapping
    public ResponseEntity<DisciplinaResponse> criar(@RequestBody @Valid CriarDisciplinaRequest request) {
        Disciplina disciplina = useCase.executar(request.nome());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(DisciplinaResponse.fromDomain(disciplina));
    }

    @DeleteMapping("/{disciplinaId}")
    public ResponseEntity<DisciplinaResponse> inativar(@PathVariable Long disciplinaId) {
        Disciplina disciplina = inativarDisciplinaUseCase.executar(disciplinaId);
        return ResponseEntity.ok(DisciplinaResponse.fromDomain(disciplina));
    }

    @PatchMapping("/{disciplinaId}/ativar")
    public ResponseEntity<DisciplinaResponse> ativar(@PathVariable Long disciplinaId) {
        Disciplina disciplina = ativarDisciplinaUseCase.executar(disciplinaId);
        return ResponseEntity.ok(DisciplinaResponse.fromDomain(disciplina));
    }
}
