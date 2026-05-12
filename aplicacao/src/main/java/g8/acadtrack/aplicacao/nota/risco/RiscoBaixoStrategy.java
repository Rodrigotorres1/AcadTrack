package g8.acadtrack.aplicacao.nota.risco;

import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class RiscoBaixoStrategy implements EstrategiaClassificacaoRiscoAcademico {

    @Override
    public boolean aplica(double mediaGeral, long simuladosComBaixoDesempenho) {
        return true;
    }

    @Override
    public NivelRiscoAcademico nivel() {
        return NivelRiscoAcademico.BAIXO;
    }
}
