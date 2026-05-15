package g8.acadtrack.aplicacao.nota.risco;

import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class RiscoModeradoStrategy implements EstrategiaClassificacaoRiscoAcademico {

    private static final double LIMIAR_MEDIA_RISCO = 6.0;

    @Override
    public boolean aplica(double mediaGeral, long simuladosComBaixoDesempenho) {
        return mediaGeral < LIMIAR_MEDIA_RISCO && simuladosComBaixoDesempenho == 1;
    }

    @Override
    public NivelRiscoAcademico nivel() {
        return NivelRiscoAcademico.MODERADO;
    }
}
