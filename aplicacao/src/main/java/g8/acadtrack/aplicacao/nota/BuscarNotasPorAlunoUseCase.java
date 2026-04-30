package g8.acadtrack.aplicacao.nota;

import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
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