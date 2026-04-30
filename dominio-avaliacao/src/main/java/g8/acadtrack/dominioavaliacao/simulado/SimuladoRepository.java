package g8.acadtrack.dominioavaliacao.simulado;

import java.util.Optional;

public interface SimuladoRepository {

    Simulado salvar(Simulado simulado);

    Optional<Simulado> buscarPorId(Long id);

    Optional<Simulado> buscarPorDescricaoNormalizada(String descricao);
}