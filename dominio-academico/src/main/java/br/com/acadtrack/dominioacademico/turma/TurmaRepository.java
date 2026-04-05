package br.com.acadtrack.dominioacademico.turma;

import java.util.Optional;

public interface TurmaRepository {

    void salvar(Turma turma);

    Optional<Turma> buscarPorId(Long id);
}