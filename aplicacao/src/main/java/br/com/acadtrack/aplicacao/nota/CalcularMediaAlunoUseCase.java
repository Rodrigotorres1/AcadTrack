package br.com.acadtrack.aplicacao.nota;

import br.com.acadtrack.dominioavaliacao.nota.Nota;
import br.com.acadtrack.dominioavaliacao.nota.NotaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalcularMediaAlunoUseCase {

    private final NotaRepository notaRepository;

    public CalcularMediaAlunoUseCase(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    public double executar(Long alunoId) {
        List<Nota> notas = notaRepository.buscarPorAlunoId(alunoId);

        if (notas.isEmpty()) {
            return 0.0;
        }

        double soma = 0.0;
        for (Nota nota : notas) {
            soma += nota.getValor();
        }

        return soma / notas.size();
    }
}