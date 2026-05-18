package g8.acadtrack.aplicacao.nota;

import g8.acadtrack.dominioacademico.aluno.SituacaoAcademica;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
public class AvaliacaoAcademicaService {

    public static final double LIMIAR_BAIXO_DESEMPENHO_SIMULADO = 5.0;

    public double calcularMediaAritmetica(List<Nota> notas) {
        if (notas == null || notas.isEmpty()) {
            return 0.0;
        }

        double soma = 0.0;
        for (Nota nota : notas) {
            soma += nota.getValor();
        }
        return arredondarMedia(soma / notas.size());
    }

    public double calcularMediaPonderada(
            List<Nota> notas,
            Map<SimuladoDisciplinaKey, Double> pesosPorSimuladoEDisciplina
    ) {
        if (notas == null || notas.isEmpty() || pesosPorSimuladoEDisciplina == null || pesosPorSimuladoEDisciplina.isEmpty()) {
            return 0.0;
        }

        double somaPonderada = 0.0;
        double somaPesos = 0.0;

        for (Nota nota : notas) {
            Double peso = pesosPorSimuladoEDisciplina.get(
                    new SimuladoDisciplinaKey(nota.getSimuladoId(), nota.getDisciplinaId())
            );
            if (peso != null) {
                somaPonderada += nota.getValor() * peso;
                somaPesos += peso;
            }
        }

        if (somaPesos == 0.0) {
            return 0.0;
        }

        return arredondarMedia(somaPonderada / somaPesos);
    }

    public boolean isBaixoDesempenhoSimulado(double mediaPonderada) {
        return mediaPonderada < LIMIAR_BAIXO_DESEMPENHO_SIMULADO;
    }

    public double arredondarMedia(double media) {
        return BigDecimal.valueOf(media)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public SituacaoAcademica calcularSituacao(double media) {
        if (media >= 7.0) {
            return SituacaoAcademica.APROVADO;
        }
        if (media >= 5.0) {
            return SituacaoAcademica.RECUPERACAO;
        }
        return SituacaoAcademica.REPROVADO;
    }

    public record SimuladoDisciplinaKey(Long simuladoId, Long disciplinaId) {
    }
}
