package g8.acadtrack.aplicacao.nota;

import g8.acadtrack.dominioacademico.aluno.SituacaoAcademica;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class AvaliacaoAcademicaService {

    public double calcularMedia(List<Nota> notas) {
        if (notas == null || notas.isEmpty()) {
            return 0.0;
        }

        double soma = 0.0;
        for (Nota nota : notas) {
            soma += nota.getValor();
        }
        return arredondarMedia(soma / notas.size());
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
}
