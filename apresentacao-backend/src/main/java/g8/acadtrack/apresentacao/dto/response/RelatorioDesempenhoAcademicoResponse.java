package g8.acadtrack.apresentacao.dto.response;

import g8.acadtrack.aplicacao.planoestudo.TipoPlanoEstudo;
import g8.acadtrack.aplicacao.relatorio.CriterioOrdenacaoRelatorio;
import g8.acadtrack.aplicacao.relatorio.RelatorioDesempenhoAcademicoItem;
import g8.acadtrack.aplicacao.relatorio.RelatorioDesempenhoAcademicoResultado;
import g8.acadtrack.dominioacademico.aluno.SituacaoAcademica;

import java.util.List;

public record RelatorioDesempenhoAcademicoResponse(
        CriterioOrdenacaoRelatorio ordenacao,
        int totalAlunosAnalisados,
        List<ItemResponse> alunos
) {
    public static RelatorioDesempenhoAcademicoResponse fromApplication(RelatorioDesempenhoAcademicoResultado resultado) {
        return new RelatorioDesempenhoAcademicoResponse(
                resultado.ordenacao(),
                resultado.totalAlunosAnalisados(),
                resultado.alunos().stream()
                        .map(ItemResponse::fromApplication)
                        .toList()
        );
    }

    public record ItemResponse(
            Long alunoId,
            String nomeAluno,
            double mediaGeral,
            String nivelRisco,
            SituacaoAcademica situacaoAcademica,
            TipoPlanoEstudo tipoPlanoEstudo
    ) {
        public static ItemResponse fromApplication(RelatorioDesempenhoAcademicoItem item) {
            return new ItemResponse(
                    item.alunoId(),
                    item.nomeAluno(),
                    item.mediaGeral(),
                    item.nivelRisco(),
                    item.situacaoAcademica(),
                    item.tipoPlanoEstudo()
            );
        }
    }
}
