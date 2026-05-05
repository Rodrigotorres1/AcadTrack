package g8.acadtrack.dominioacademico.aluno;

import java.util.List;
import java.util.Optional;

public interface AlunoRepository {

    Aluno salvar(Aluno aluno);

    Optional<Aluno> buscarPorId(Long id);

    List<Aluno> buscarTodos();

    boolean existeAlunoComEmailIgnorandoMaiusculas(String email);
}
