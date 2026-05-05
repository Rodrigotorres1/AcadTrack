package g8.acadtrack.aplicacao.relatorio;

import java.util.Comparator;
import java.util.List;

public class MelhorMediaGeralIterator implements IteratorAcademico<RelatorioDesempenhoAcademicoItem> {

    private final List<RelatorioDesempenhoAcademicoItem> itensOrdenados;
    private int posicao;

    public MelhorMediaGeralIterator(List<RelatorioDesempenhoAcademicoItem> itens) {
        this.itensOrdenados = itens.stream()
                .sorted(Comparator.comparingDouble(RelatorioDesempenhoAcademicoItem::mediaGeral).reversed())
                .toList();
    }

    @Override
    public boolean temProximo() {
        return posicao < itensOrdenados.size();
    }

    @Override
    public RelatorioDesempenhoAcademicoItem proximo() {
        return itensOrdenados.get(posicao++);
    }
}
