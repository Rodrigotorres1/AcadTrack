package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.responsavel.CriarResponsavelUseCase;
import br.com.acadtrack.aplicacao.responsavel.ConsultarDesempenhoAlunoPorResponsavelUseCase;
import br.com.acadtrack.aplicacao.responsavel.ConsultarNotasAlunoPorResponsavelUseCase;
import br.com.acadtrack.aplicacao.responsavel.ConsultarSimuladosAlunoPorResponsavelUseCase;
import br.com.acadtrack.apresentacao.dto.request.CriarResponsavelRequest;
import br.com.acadtrack.apresentacao.dto.response.AnaliseDesempenhoResponse;
import br.com.acadtrack.apresentacao.dto.response.NotaResponse;
import br.com.acadtrack.apresentacao.dto.response.ResponsavelResponse;
import br.com.acadtrack.dominioavaliacao.nota.Nota;
import br.com.acadtrack.dominiousuarios.responsavel.Responsavel;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/responsaveis")
public class ResponsavelController {

    private final CriarResponsavelUseCase criarResponsavelUseCase;
    private final ConsultarNotasAlunoPorResponsavelUseCase consultarNotasAlunoPorResponsavelUseCase;
    private final ConsultarSimuladosAlunoPorResponsavelUseCase consultarSimuladosAlunoPorResponsavelUseCase;
    private final ConsultarDesempenhoAlunoPorResponsavelUseCase consultarDesempenhoAlunoPorResponsavelUseCase;

    public ResponsavelController(
            CriarResponsavelUseCase criarResponsavelUseCase,
            ConsultarNotasAlunoPorResponsavelUseCase consultarNotasAlunoPorResponsavelUseCase,
            ConsultarSimuladosAlunoPorResponsavelUseCase consultarSimuladosAlunoPorResponsavelUseCase,
            ConsultarDesempenhoAlunoPorResponsavelUseCase consultarDesempenhoAlunoPorResponsavelUseCase
    ) {
        this.criarResponsavelUseCase = criarResponsavelUseCase;
        this.consultarNotasAlunoPorResponsavelUseCase = consultarNotasAlunoPorResponsavelUseCase;
        this.consultarSimuladosAlunoPorResponsavelUseCase = consultarSimuladosAlunoPorResponsavelUseCase;
        this.consultarDesempenhoAlunoPorResponsavelUseCase = consultarDesempenhoAlunoPorResponsavelUseCase;
    }

    @PostMapping
    public ResponseEntity<ResponsavelResponse> criar(@RequestBody @Valid CriarResponsavelRequest request) {
        Responsavel responsavel = criarResponsavelUseCase.executar(
                request.getNome(),
                request.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponsavelResponse.fromDomain(responsavel));
    }

    @GetMapping("/{responsavelId}/alunos/{alunoId}/notas")
    public ResponseEntity<List<NotaResponse>> consultarNotas(
            @PathVariable Long responsavelId,
            @PathVariable Long alunoId
    ) {
        List<Nota> notas = consultarNotasAlunoPorResponsavelUseCase.executar(responsavelId, alunoId);
        return ResponseEntity.ok(notas.stream().map(NotaResponse::fromDomain).toList());
    }

    @GetMapping("/{responsavelId}/alunos/{alunoId}/simulados")
    public ResponseEntity<List<Long>> consultarSimulados(
            @PathVariable Long responsavelId,
            @PathVariable Long alunoId
    ) {
        return ResponseEntity.ok(consultarSimuladosAlunoPorResponsavelUseCase.executar(responsavelId, alunoId));
    }

    @GetMapping("/{responsavelId}/alunos/{alunoId}/desempenho")
    public ResponseEntity<AnaliseDesempenhoResponse> consultarDesempenho(
            @PathVariable Long responsavelId,
            @PathVariable Long alunoId
    ) {
        return ResponseEntity.ok(
                AnaliseDesempenhoResponse.fromApplication(
                        consultarDesempenhoAlunoPorResponsavelUseCase.executar(responsavelId, alunoId)
                )
        );
    }
}