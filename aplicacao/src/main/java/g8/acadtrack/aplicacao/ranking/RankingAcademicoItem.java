package g8.acadtrack.aplicacao.ranking;

public record RankingAcademicoItem(
        Long alunoId,
        String nomeAluno,
        double media,
        int posicao,
        String situacaoAcademica,
        String nivelRisco
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
