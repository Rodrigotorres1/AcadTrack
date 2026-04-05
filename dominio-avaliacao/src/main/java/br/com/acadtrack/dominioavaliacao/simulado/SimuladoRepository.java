package br.com.acadtrack.dominioavaliacao.simulado;

import java.util.Optional;

public interface SimuladoRepository {

    Simulado salvar(Simulado simulado);

    Optional<Simulado> buscarPorId(Long id);
}