package g8.acadtrack.aplicacao.riscoacademico;

import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;

public record RiscoAcademicoEvent(
        Long alunoId,
        double mediaGeral,
        NivelRiscoAcademico nivelRisco,
        String situacaoAcademica,
        Integer posicaoRanking,
        boolean alunoNoTop10
) {
}
