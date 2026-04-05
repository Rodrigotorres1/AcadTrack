package br.com.acadtrack.dominioacademico.simulado;

import java.util.Optional;

public interface SimuladoRepository {

    void salvar(Simulado simulado);

    Optional<Simulado> buscarPorId(Long id);

}