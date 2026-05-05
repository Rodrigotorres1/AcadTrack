package g8.acadtrack.aplicacao.planoestudo;

import g8.acadtrack.aplicacao.nota.AnaliseDesempenhoAcademicoResultado;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelecionadorPlanoEstudoService {

    private final List<PlanoEstudoRecomendacaoStrategy> estrategias;

    public SelecionadorPlanoEstudoService(List<PlanoEstudoRecomendacaoStrategy> estrategias) {
        this.estrategias = estrategias;
    }

    public PlanoEstudoRecomendadoResultado recomendar(AnaliseDesempenhoAcademicoResultado analise) {
        return estrategias.stream()
                .filter(estrategia -> estrategia.deveAplicar(analise))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Nao foi encontrada estrategia de plano de estudo para a analise informada"))
                .recomendar(analise);
    }
}
