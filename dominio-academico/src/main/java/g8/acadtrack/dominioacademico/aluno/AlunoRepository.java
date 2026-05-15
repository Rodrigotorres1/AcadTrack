package g8.acadtrack.dominioacademico.aluno;

import java.util.List;
import java.util.Optional;

public interface AlunoRepository {

    Aluno salvar(Aluno aluno);

    Optional<Aluno> buscarPorId(Long id);

    List<Aluno> buscarPorIds(List<Long> ids);

    List<Aluno> buscarTodos();

    List<Aluno> buscarPorResponsavelId(Long responsavelId);

    boolean existeAlunoComEmailIgnorandoMaiusculas(String email);
}
