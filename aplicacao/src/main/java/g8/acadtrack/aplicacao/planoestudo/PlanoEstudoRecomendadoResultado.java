package g8.acadtrack.aplicacao.planoestudo;

import java.util.List;

public record PlanoEstudoRecomendadoResultado(
        Long alunoId,
        double mediaGeral,
        String nivelRisco,
        TipoPlanoEstudo tipoPlano,
        String descricao,
        int cargaHorariaSemanalSugerida,
        List<String> orientacoes
) {
}
