package g8.acadtrack.aplicacao.nota.risco;

import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassificadorRiscoAcademicoService {

    private final List<EstrategiaClassificacaoRiscoAcademico> estrategias;

    public ClassificadorRiscoAcademicoService(List<EstrategiaClassificacaoRiscoAcademico> estrategias) {
        this.estrategias = estrategias;
    }

    public NivelRiscoAcademico classificar(double mediaGeral, long simuladosComBaixoDesempenho) {
        return estrategias.stream()
                .filter(estrategia -> estrategia.aplica(mediaGeral, simuladosComBaixoDesempenho))
                .findFirst()
                .map(EstrategiaClassificacaoRiscoAcademico::nivel)
                .orElse(NivelRiscoAcademico.BAIXO);
    }
}
