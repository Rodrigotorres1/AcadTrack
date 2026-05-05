package g8.acadtrack.aplicacao.planoestudo;

import g8.acadtrack.aplicacao.nota.AnaliseDesempenhoAcademicoResultado;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlanoManutencaoStrategy implements PlanoEstudoRecomendacaoStrategy {

    @Override
    public boolean deveAplicar(AnaliseDesempenhoAcademicoResultado analise) {
        return "BAIXO".equals(analise.nivelRisco()) && analise.mediaGeral() < 8.0;
    }

    @Override
    public PlanoEstudoRecomendadoResultado recomendar(AnaliseDesempenhoAcademicoResultado analise) {
        return new PlanoEstudoRecomendadoResultado(
                analise.alunoId(),
                analise.mediaGeral(),
                analise.nivelRisco(),
                TipoPlanoEstudo.PLANO_MANUTENCAO,
                "Plano de manutencao para preservar desempenho estavel.",
                5,
                List.of(
                        "Manter rotina semanal de revisao dos principais conteudos.",
                        "Resolver questoes de fixacao antes dos proximos simulados.",
                        "Monitorar disciplinas com media mais proxima do limite de risco."
                )
        );
    }
}
