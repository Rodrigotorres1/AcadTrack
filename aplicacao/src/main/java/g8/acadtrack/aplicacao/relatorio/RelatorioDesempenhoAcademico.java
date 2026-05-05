package g8.acadtrack.aplicacao.relatorio;

import java.util.List;

public class RelatorioDesempenhoAcademico {

    private final List<RelatorioDesempenhoAcademicoItem> itens;

    public RelatorioDesempenhoAcademico(List<RelatorioDesempenhoAcademicoItem> itens) {
        this.itens = List.copyOf(itens);
    }

    public IteratorAcademico<RelatorioDesempenhoAcademicoItem> iterator(CriterioOrdenacaoRelatorio criterio) {
        return switch (CriterioOrdenacaoRelatorio.padraoSeNulo(criterio)) {
            case MAIOR_RISCO -> new MaiorRiscoAcademicoIterator(itens);
            case MENOR_MEDIA -> new MenorMediaGeralIterator(itens);
            case MELHOR_MEDIA -> new MelhorMediaGeralIterator(itens);
        };
    }

    public int totalItens() {
        return itens.size();
    }
}
