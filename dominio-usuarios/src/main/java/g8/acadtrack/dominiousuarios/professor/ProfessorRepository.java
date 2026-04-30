package g8.acadtrack.dominiousuarios.professor;

import java.util.Optional;

public interface ProfessorRepository {

    Professor salvar(Professor professor);

    Optional<Professor> buscarPorId(Long id);
}