package br.com.acadtrack.aplicacao.nota;

import br.com.acadtrack.dominioavaliacao.nota.Nota;
import br.com.acadtrack.dominioavaliacao.nota.NotaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuscarNotasPorAlunoUseCase {

    private final NotaRepository notaRepository;

    public BuscarNotasPorAlunoUseCase(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    public List<Nota> executar(Long alunoId) {
        return notaRepository.buscarPorAlunoId(alunoId);
    }
}