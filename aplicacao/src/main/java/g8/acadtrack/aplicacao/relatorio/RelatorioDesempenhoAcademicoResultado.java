package g8.acadtrack.aplicacao.relatorio;

import java.util.List;

public record RelatorioDesempenhoAcademicoResultado(
        CriterioOrdenacaoRelatorio ordenacao,
        int totalAlunosAnalisados,
        List<RelatorioDesempenhoAcademicoItem> alunos
) {
}
