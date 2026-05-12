package g8.acadtrack.aplicacao.ranking;

import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;

public record RankingAcademicoItem(
        Long alunoId,
        String nomeAluno,
        double media,
        int posicao,
        String situacaoAcademica,
        NivelRiscoAcademico nivelRisco
) {
    public RankingAcademicoItem comPosicao(int novaPosicao) {
        return new RankingAcademicoItem(
                alunoId,
                nomeAluno,
                media,
                novaPosicao,
                situacaoAcademica,
                nivelRisco
        );
    }
}
