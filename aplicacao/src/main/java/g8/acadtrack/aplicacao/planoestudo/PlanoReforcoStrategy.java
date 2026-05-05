package g8.acadtrack.aplicacao.planoestudo;

import g8.acadtrack.aplicacao.nota.AnaliseDesempenhoAcademicoResultado;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlanoReforcoStrategy implements PlanoEstudoRecomendacaoStrategy {

    @Override
    public boolean deveAplicar(AnaliseDesempenhoAcademicoResultado analise) {
        return "MODERADO".equals(analise.nivelRisco());
    }

    @Override
    public PlanoEstudoRecomendadoResultado recomendar(AnaliseDesempenhoAcademicoResultado analise) {
        return new PlanoEstudoRecomendadoResultado(
                analise.alunoId(),
                analise.mediaGeral(),
                analise.nivelRisco(),
                TipoPlanoEstudo.PLANO_REFORCO,
                "Plano de reforco para consolidar pontos fragilizados.",
                8,
                List.of(
                        "Priorizar simulados ou disciplinas com baixo desempenho.",
                        "Fazer revisoes curtas e frequentes dos topicos recentes.",
                        "Acompanhar a evolucao da media a cada novo lancamento de nota."
                )
        );
    }
}
