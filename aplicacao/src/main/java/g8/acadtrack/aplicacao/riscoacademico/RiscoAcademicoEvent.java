package g8.acadtrack.aplicacao.riscoacademico;

public record RiscoAcademicoEvent(
        Long alunoId,
        double mediaGeral,
        String nivelRisco,
        String situacaoAcademica,
        Integer posicaoRanking,
        boolean alunoNoTop10
) {
}
