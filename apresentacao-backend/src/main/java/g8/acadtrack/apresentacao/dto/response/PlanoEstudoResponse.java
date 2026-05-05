package g8.acadtrack.apresentacao.dto.response;

import g8.acadtrack.aplicacao.planoestudo.PlanoEstudoRecomendadoResultado;
import g8.acadtrack.aplicacao.planoestudo.TipoPlanoEstudo;

import java.util.List;

public record PlanoEstudoResponse(
        Long alunoId,
        double mediaGeral,
        String nivelRisco,
        TipoPlanoEstudo tipoPlano,
        String descricao,
        int cargaHorariaSemanalSugerida,
        List<String> orientacoes
) {
    public static PlanoEstudoResponse fromApplication(PlanoEstudoRecomendadoResultado resultado) {
        return new PlanoEstudoResponse(
                resultado.alunoId(),
                resultado.mediaGeral(),
                resultado.nivelRisco(),
                resultado.tipoPlano(),
                resultado.descricao(),
                resultado.cargaHorariaSemanalSugerida(),
                resultado.orientacoes()
        );
    }
}
