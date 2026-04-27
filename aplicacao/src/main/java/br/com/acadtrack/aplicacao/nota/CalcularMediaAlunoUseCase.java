package br.com.acadtrack.aplicacao.nota;

import br.com.acadtrack.dominioavaliacao.nota.Nota;
import br.com.acadtrack.dominioavaliacao.nota.NotaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalcularMediaAlunoUseCase {

    private final NotaRepository notaRepository;
    private final AvaliacaoAcademicaService avaliacaoAcademicaService;

    public CalcularMediaAlunoUseCase(
            NotaRepository notaRepository,
            AvaliacaoAcademicaService avaliacaoAcademicaService
    ) {
        this.notaRepository = notaRepository;
        this.avaliacaoAcademicaService = avaliacaoAcademicaService;
    }

    public double executar(Long alunoId) {
        List<Nota> notas = notaRepository.buscarPorAlunoId(alunoId);
        return avaliacaoAcademicaService.calcularMedia(notas);
    }
}