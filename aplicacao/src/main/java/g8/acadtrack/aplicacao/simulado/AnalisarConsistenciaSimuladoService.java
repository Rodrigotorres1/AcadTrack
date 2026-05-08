package g8.acadtrack.aplicacao.simulado;

import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalisarConsistenciaSimuladoService {

    private static final int MINIMO_DISCIPLINAS = 2;

    public ConsistenciaSimuladoResultado analisar(
            List<SimuladoDisciplina> vinculos,
            List<Disciplina> disciplinas
    ) {
        if (vinculos == null || vinculos.isEmpty()) {
            return inconsistente("Este simulado possui problemas de consistencia. Nenhuma disciplina vinculada.");
        }

        long disciplinasDistintas = vinculos.stream()
                .map(SimuladoDisciplina::getDisciplinaId)
                .distinct()
                .count();

        if (disciplinasDistintas != vinculos.size()) {
            return inconsistente("Este simulado possui problemas de consistencia. Contem disciplinas repetidas.");
        }

        if (disciplinasDistintas < MINIMO_DISCIPLINAS) {
            return inconsistente("Este simulado possui problemas de consistencia. Possui menos de 2 disciplinas.");
        }

        if (disciplinas == null || disciplinas.size() != disciplinasDistintas) {
            return inconsistente("Este simulado possui problemas de consistencia. Contem disciplinas inexistentes.");
        }

        boolean possuiInativa = disciplinas.stream().anyMatch(disciplina -> !disciplina.estaAtiva());
        if (possuiInativa) {
            return inconsistente("Este simulado possui problemas de consistencia. Contem disciplinas inativas.");
        }

        return new ConsistenciaSimuladoResultado(true, null);
    }

    private ConsistenciaSimuladoResultado inconsistente(String motivo) {
        return new ConsistenciaSimuladoResultado(false, motivo);
    }
}
