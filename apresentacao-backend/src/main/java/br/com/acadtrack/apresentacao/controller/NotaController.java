package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.nota.LancarNotaUseCase;
import br.com.acadtrack.apresentacao.dto.LancarNotaRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notas")
public class NotaController {

    private final LancarNotaUseCase lancarNotaUseCase;

    public NotaController(LancarNotaUseCase lancarNotaUseCase) {
        this.lancarNotaUseCase = lancarNotaUseCase;
    }

    @PostMapping
    public ResponseEntity<String> lancar(@RequestBody LancarNotaRequest request) {
        lancarNotaUseCase.executar(
                request.getId(),
                request.getAlunoId(),
                request.getSimuladoId(),
                request.getDisciplina(),
                request.getValor()
        );
        return ResponseEntity.ok("Nota lançada com sucesso");
    }
}