package g8.acadtrack.aplicacao.nota.risco;

import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class RiscoAltoStrategy implements EstrategiaClassificacaoRiscoAcademico {

    private static final double LIMIAR_RISCO_ALTO_MEDIA = 5.0;

    @Override
    public boolean aplica(double mediaGeral, long simuladosComBaixoDesempenho) {
        return mediaGeral < LIMIAR_RISCO_ALTO_MEDIA || simuladosComBaixoDesempenho >= 2;
    }

    @Override
    public NivelRiscoAcademico nivel() {
        return NivelRiscoAcademico.ALTO;
    }
}
