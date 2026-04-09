package br.com.acadtrack.dominioacademico.disciplina;

import java.util.List;
import java.util.Optional;

public interface DisciplinaRepository {

    Disciplina salvar(Disciplina disciplina);

    Optional<Disciplina> buscarPorId(List<Long> disciplinasIds);

    Optional<Disciplina> buscarPorId(Long id);

    List<Disciplina> buscarporIds(List<Long> ids);

    List<Disciplina> buscarPorIds(List<Long> ids);

    List<Disciplina> buscarTodos();
}