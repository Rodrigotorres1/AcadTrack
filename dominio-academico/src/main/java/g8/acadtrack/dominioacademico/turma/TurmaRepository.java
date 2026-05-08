package g8.acadtrack.dominioacademico.turma;

import java.util.List;
import java.util.Optional;

public interface TurmaRepository {

    Turma salvar(Turma turma);

    List<Turma> buscarTodos();

    Optional<Turma> buscarPorId(Long id);

    boolean existeComNomeNormalizado(String nomeNormalizado);

    void excluirPorId(Long id);
}
