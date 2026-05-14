package g8.acadtrack.aplicacao.riscoacademico;

import g8.acadtrack.dominioacademico.aluno.SituacaoAcademica;
import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;

public record RiscoAcademicoEvent(
        Long alunoId,
        double mediaGeral,
        NivelRiscoAcademico nivelRisco,
        SituacaoAcademica situacaoAcademica,
        Integer posicaoRanking,
        boolean alunoNoTop10
) {
}
