package br.com.acadtrack.dominiousuarios.responsavel;

import java.util.Optional;

public interface ResponsavelRepository {

    void salvar(Responsavel responsavel);

    Optional<Responsavel> buscarPorId(Long id);

    Optional<Responsavel> buscarPorAlunoId(Long alunoId);

    void vincularAoAluno(Long responsavelId, Long alunoId);

    void desvincularDoAluno(Long alunoId);
}