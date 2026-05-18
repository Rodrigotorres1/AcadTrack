package g8.acadtrack.dominioavaliacao.simulado;

import java.util.List;
import java.util.Optional;

public interface SimuladoRepository {

    Simulado salvar(Simulado simulado);

    List<Simulado> buscarTodos();

    List<Simulado> buscarPorIds(List<Long> ids);

    List<SimuladoDisciplina> buscarPesosDisciplinasPorSimuladoIds(List<Long> simuladoIds);

    Optional<Simulado> buscarPorId(Long id);

    Optional<Simulado> buscarPorDescricaoNormalizada(String descricao);
}
