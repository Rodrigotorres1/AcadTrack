package g8.acadtrack.aplicacao.planoestudo;

import g8.acadtrack.aplicacao.nota.AnaliseDesempenhoAcademicoResultado;

public interface PlanoEstudoRecomendacaoStrategy {

    boolean deveAplicar(AnaliseDesempenhoAcademicoResultado analise);

    PlanoEstudoRecomendadoResultado recomendar(AnaliseDesempenhoAcademicoResultado analise);
}
