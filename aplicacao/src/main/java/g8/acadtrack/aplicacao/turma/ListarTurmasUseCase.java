package g8.acadtrack.aplicacao.turma;

import g8.acadtrack.dominioacademico.turma.Turma;
import g8.acadtrack.dominioacademico.turma.TurmaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarTurmasUseCase {

    private final TurmaRepository turmaRepository;

    public ListarTurmasUseCase(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }

    public List<Turma> executar() {
        return turmaRepository.buscarTodos();
    }
}
