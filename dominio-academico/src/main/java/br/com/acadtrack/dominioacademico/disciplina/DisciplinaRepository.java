package br.com.acadtrack.dominioacademico.disciplina;

import java.util.Optional;

public interface DisciplinaRepository {

    Disciplina salvar(Disciplina disciplina);

    Optional<Disciplina> buscarPorId(Long id);
}