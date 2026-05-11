package g8.acadtrack.aplicacao.simulado;

import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidarComposicaoSimuladoService {

    private static final int MINIMO_DISCIPLINAS = 2;

    public void validarDisciplinasParaCriacao(List<Long> disciplinasIds) {
        if (disciplinasIds == null || disciplinasIds.isEmpty()) {
            throw new RegraDeNegocioException("O simulado deve possuir pelo menos duas disciplinas");
        }

        long disciplinasDistintas = disciplinasIds.stream().distinct().count();
        if (disciplinasDistintas != disciplinasIds.size()) {
            throw new RegraDeNegocioException("Não é permitido vincular disciplina repetida no mesmo simulado");
        }

        if (disciplinasDistintas < MINIMO_DISCIPLINAS) {
            throw new RegraDeNegocioException("O simulado deve possuir pelo menos duas disciplinas distintas");
        }
    }
}
