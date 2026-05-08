package g8.acadtrack.apresentacao.dto.response;

import g8.acadtrack.aplicacao.ranking.RankingAcademicoItem;

public record RankingAcademicoResponse(
        Long alunoId,
        String nomeAluno,
        double media,
        int posicao,
        String situacaoAcademica,
        String nivelRisco
) {
    public static RankingAcademicoResponse fromApplication(RankingAcademicoItem item) {
        return new RankingAcademicoResponse(
                item.alunoId(),
                item.nomeAluno(),
                item.media(),
                item.posicao(),
                item.situacaoAcademica(),
                item.nivelRisco()
        );
    }
}
