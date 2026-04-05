package br.com.acadtrack.dominioacademico.aluno;

import java.util.Optional;

public interface AlunoRepository {

    Aluno salvar(Aluno aluno);

    Optional<Aluno> buscarPorId(Long id);

}