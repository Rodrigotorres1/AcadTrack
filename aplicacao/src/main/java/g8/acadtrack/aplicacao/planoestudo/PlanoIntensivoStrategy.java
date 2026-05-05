package g8.acadtrack.aplicacao.planoestudo;

import g8.acadtrack.aplicacao.nota.AnaliseDesempenhoAcademicoResultado;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlanoIntensivoStrategy implements PlanoEstudoRecomendacaoStrategy {

    @Override
    public boolean deveAplicar(AnaliseDesempenhoAcademicoResultado analise) {
        return "ALTO".equals(analise.nivelRisco());
    }

    @Override
    public PlanoEstudoRecomendadoResultado recomendar(AnaliseDesempenhoAcademicoResultado analise) {
        return new PlanoEstudoRecomendadoResultado(
                analise.alunoId(),
                analise.mediaGeral(),
                analise.nivelRisco(),
                TipoPlanoEstudo.PLANO_INTENSIVO,
                "Plano intensivo para recuperacao rapida do desempenho academico.",
                12,
                List.of(
                        "Revisar conteudos basicos das disciplinas com menor desempenho.",
                        "Resolver exercicios orientados ao final de cada ciclo de estudo.",
                        "Realizar acompanhamento semanal com professor ou monitor."
                )
        );
    }
}
