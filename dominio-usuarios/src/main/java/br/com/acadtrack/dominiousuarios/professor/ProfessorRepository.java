package br.com.acadtrack.dominiousuarios.professor;

import java.util.Optional;

public interface ProfessorRepository {

    void salvar(Professor professor);

    Optional<Professor> buscarPorId(Long id);
}