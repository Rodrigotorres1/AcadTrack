package g8.acadtrack.dominioavaliacao.simulado;

import java.util.List;

public interface SimuladoDisciplinaRepository {

    SimuladoDisciplina salvar(SimuladoDisciplina simuladoDisciplina);

    List<SimuladoDisciplina> buscarPorSimulado(Long simuladoId);

    List<SimuladoDisciplina> buscarPorSimuladoIds(List<Long> simuladoIds);

    void excluirPorSimulado(Long simuladoId);
}
