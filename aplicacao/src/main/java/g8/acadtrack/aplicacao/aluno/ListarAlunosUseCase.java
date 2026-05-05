package g8.acadtrack.aplicacao.aluno;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarAlunosUseCase {

    private final AlunoRepository alunoRepository;

    public ListarAlunosUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public List<Aluno> executar() {
        return alunoRepository.buscarTodos();
    }
}
