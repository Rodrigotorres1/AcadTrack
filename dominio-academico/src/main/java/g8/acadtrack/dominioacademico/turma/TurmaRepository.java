package g8.acadtrack.dominioacademico.turma;

import java.util.Optional;

public interface TurmaRepository {

    Turma salvar(Turma turma);

    Optional<Turma> buscarPorId(Long id);
}