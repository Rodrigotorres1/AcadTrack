package br.com.acadtrack.apresentacao.controller;

import br.com.acadtrack.aplicacao.nota.BuscarNotasPorAlunoUseCase;
import br.com.acadtrack.aplicacao.nota.CalcularMediaPonderadaUseCase;
import br.com.acadtrack.aplicacao.nota.LancarNotaUseCase;
import br.com.acadtrack.aplicacao.nota.RankingAlunosUseCase;
import br.com.acadtrack.apresentacao.dto.LancarNotaRequest;
import br.com.acadtrack.apresentacao.dto.NotaResponse;
import br.com.acadtrack.dominioavaliacao.nota.Nota;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notas")
public class NotaController {

    private final LancarNotaUseCase lancarNotaUseCase;
    private final BuscarNotasPorAlunoUseCase buscarNotasPorAlunoUseCase;
    private final CalcularMediaPonderadaUseCase calcularMediaPonderadaUseCase;
    private final RankingAlunosUseCase rankingAlunosUseCase;

    public NotaController(LancarNotaUseCase lancarNotaUseCase,
                          BuscarNotasPorAlunoUseCase buscarNotasPorAlunoUseCase,
                          CalcularMediaPonderadaUseCase calcularMediaPonderadaUseCase,
                          RankingAlunosUseCase rankingAlunosUseCase) {
        this.lancarNotaUseCase = lancarNotaUseCase;
        this.buscarNotasPorAlunoUseCase = buscarNotasPorAlunoUseCase;
        this.calcularMediaPonderadaUseCase = calcularMediaPonderadaUseCase;
        this.rankingAlunosUseCase = rankingAlunosUseCase;
    }

    @PostMapping
    public ResponseEntity<NotaResponse> criar(@RequestBody LancarNotaRequest request) {
        Nota nota = lancarNotaUseCase.executar(
                request.getAlunoId(),
                request.getSimuladoId(),
                request.getDisciplinaId(),
                request.getValor()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(NotaResponse.fromDomain(nota));
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<NotaResponse>> buscarPorAluno(@PathVariable Long alunoId) {
        List<NotaResponse> response = buscarNotasPorAlunoUseCase.executar(alunoId)
                .stream()
                .map(NotaResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/aluno/{alunoId}/simulado/{simuladoId}/media")
    public ResponseEntity<Double> calcularMediaPonderada(@PathVariable Long alunoId,
                                                         @PathVariable Long simuladoId) {
        double media = calcularMediaPonderadaUseCase.executar(alunoId, simuladoId);
        return ResponseEntity.ok(media);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<Map<String, Object>>> ranking() {
        return ResponseEntity.ok(rankingAlunosUseCase.executar());
    }

    @GetMapping("/ranking/top")
    public ResponseEntity<Map<String, Object>> top() {
        return ResponseEntity.ok(rankingAlunosUseCase.executar().get(0));
    }
}