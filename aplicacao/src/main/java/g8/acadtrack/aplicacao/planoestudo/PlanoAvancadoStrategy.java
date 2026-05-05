package g8.acadtrack.aplicacao.planoestudo;

import g8.acadtrack.aplicacao.nota.AnaliseDesempenhoAcademicoResultado;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlanoAvancadoStrategy implements PlanoEstudoRecomendacaoStrategy {

    @Override
    public boolean deveAplicar(AnaliseDesempenhoAcademicoResultado analise) {
        return "BAIXO".equals(analise.nivelRisco()) && analise.mediaGeral() >= 8.0;
    }

    @Override
    public PlanoEstudoRecomendadoResultado recomendar(AnaliseDesempenhoAcademicoResultado analise) {
        return new PlanoEstudoRecomendadoResultado(
                analise.alunoId(),
                analise.mediaGeral(),
                analise.nivelRisco(),
                TipoPlanoEstudo.PLANO_AVANCADO,
                "Plano avancado para ampliar dominio e aprofundamento.",
                4,
                List.of(
                        "Explorar exercicios de maior complexidade.",
                        "Aprofundar temas com melhor desempenho para manter excelencia.",
                        "Participar de desafios, simulados extras ou atividades avancadas."
                )
        );
    }
}
