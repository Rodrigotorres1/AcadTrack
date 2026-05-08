package g8.acadtrack.aplicacao.nota.risco;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassificadorRiscoAcademicoService {

    private final List<EstrategiaClassificacaoRiscoAcademico> estrategias = List.of(
            new RiscoAltoStrategy(),
            new RiscoModeradoStrategy(),
            new RiscoBaixoStrategy()
    );

    public String classificar(double mediaGeral, long simuladosComBaixoDesempenho) {
        return estrategias.stream()
                .filter(estrategia -> estrategia.aplica(mediaGeral, simuladosComBaixoDesempenho))
                .findFirst()
                .map(EstrategiaClassificacaoRiscoAcademico::nivel)
                .orElse("BAIXO");
    }
}
