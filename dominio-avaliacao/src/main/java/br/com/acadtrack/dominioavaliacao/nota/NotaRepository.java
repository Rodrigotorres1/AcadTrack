package br.com.acadtrack.dominioavaliacao.nota;

import java.util.List;
import java.util.Optional;

public interface NotaRepository {

    Nota salvar(Nota nota);

    Optional<Nota> buscarPorId(Long id);

    List<Nota> buscarPorAlunoId(Long alunoId);

    List<Nota> buscarPorSimuladoId(Long simuladoId);

    List<Nota> buscarTodas();

}