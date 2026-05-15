package g8.acadtrack.dominioavaliacao.simulado;

import java.util.List;

public interface SimuladoDisciplinaRepository {

    List<SimuladoDisciplina> buscarPorSimulado(Long simuladoId);

    List<SimuladoDisciplina> buscarPorSimuladoIds(List<Long> simuladoIds);
}
