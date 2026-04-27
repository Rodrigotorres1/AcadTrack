package br.com.acadtrack.dominioacademico.disciplina;

import java.util.List;
import java.util.Optional;

public interface DisciplinaRepository {

    Disciplina salvar(Disciplina disciplina);

    Optional<Disciplina> buscarPorId(Long id);

    Optional<Disciplina> buscarPorNomeNormalizado(String nomeNormalizado);

    List<Disciplina> buscarPorIds(List<Long> ids);

    List<Disciplina> buscarTodos();

    boolean possuiVinculoAcademico(Long disciplinaId);
}
