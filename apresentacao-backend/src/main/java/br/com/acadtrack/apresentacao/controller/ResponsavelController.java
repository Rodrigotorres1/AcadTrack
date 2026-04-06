package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.responsavel.CriarResponsavelUseCase;
import br.com.acadtrack.apresentacao.dto.CriarResponsavelRequest;
import br.com.acadtrack.apresentacao.dto.ResponsavelResponse;
import br.com.acadtrack.dominiousuarios.responsavel.Responsavel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/responsaveis")
public class ResponsavelController {

    private final CriarResponsavelUseCase criarResponsavelUseCase;

    public ResponsavelController(CriarResponsavelUseCase criarResponsavelUseCase) {
        this.criarResponsavelUseCase = criarResponsavelUseCase;
    }

    @PostMapping
    public ResponseEntity<ResponsavelResponse> criar(@RequestBody CriarResponsavelRequest request) {
        Responsavel responsavel = criarResponsavelUseCase.executar(
                request.getNome(),
                request.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponsavelResponse.fromDomain(responsavel));
    }
}