package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import br.com.acadtrack.dominioacademico.disciplina.Disciplina;
import org.springframework.web.bind.annotation.*;
import br.com.acadtrack.apresentacao.dto.CriarDisciplinaRequest;


@RestController
@RequestMapping("/disciplinas")
public class DisciplinaController {

    private final CriarDisciplinaUseCase useCase;

    public DisciplinaController(CriarDisciplinaUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public Disciplina criar(@RequestBody CriarDisciplinaRequest request) {
        return useCase.executar(request.nome());
    }
}