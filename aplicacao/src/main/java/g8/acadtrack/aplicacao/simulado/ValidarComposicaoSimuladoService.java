package g8.acadtrack.aplicacao.simulado;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidarComposicaoSimuladoService {

    private static final int MINIMO_DISCIPLINAS = 2;

    public void validarDisciplinasParaCriacao(List<Long> disciplinasIds) {
        if (disciplinasIds == null || disciplinasIds.isEmpty()) {
            throw new IllegalArgumentException("O simulado deve possuir pelo menos uma disciplina");
        }

        long disciplinasDistintas = disciplinasIds.stream().distinct().count();
        if (disciplinasDistintas != disciplinasIds.size()) {
            throw new IllegalArgumentException("Não é permitido vincular disciplina repetida no mesmo simulado");
        }

        if (disciplinasDistintas < MINIMO_DISCIPLINAS) {
            throw new IllegalArgumentException("O simulado deve possuir pelo menos duas disciplinas distintas");
        }
    }
}
