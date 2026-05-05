package g8.acadtrack.aplicacao.relatorio;

import g8.acadtrack.aplicacao.planoestudo.TipoPlanoEstudo;
import g8.acadtrack.dominioacademico.aluno.SituacaoAcademica;

public record RelatorioDesempenhoAcademicoItem(
        Long alunoId,
        String nomeAluno,
        double mediaGeral,
        String nivelRisco,
        SituacaoAcademica situacaoAcademica,
        TipoPlanoEstudo tipoPlanoEstudo
) {
}
