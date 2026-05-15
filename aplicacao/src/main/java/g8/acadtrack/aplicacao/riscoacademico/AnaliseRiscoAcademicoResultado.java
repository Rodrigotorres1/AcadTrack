package g8.acadtrack.aplicacao.riscoacademico;

import g8.acadtrack.dominioacademico.aluno.SituacaoAcademica;
import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;

public record AnaliseRiscoAcademicoResultado(
        Long alunoId,
        double mediaGeral,
        long simuladosComBaixoDesempenho,
        boolean riscoAcademico,
        NivelRiscoAcademico nivelRisco,
        SituacaoAcademica situacaoAcademica
) {
}
