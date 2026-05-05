package g8.acadtrack.aplicacao.disciplina;

import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarDisciplinasUseCase {

    private final DisciplinaRepository disciplinaRepository;

    public ListarDisciplinasUseCase(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    public List<Disciplina> executar() {
        return disciplinaRepository.buscarTodos();
    }
}
