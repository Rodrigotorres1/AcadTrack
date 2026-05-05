package g8.acadtrack.aplicacao.relatorio;

import java.util.Comparator;
import java.util.List;

public class MaiorRiscoAcademicoIterator implements IteratorAcademico<RelatorioDesempenhoAcademicoItem> {

    private final List<RelatorioDesempenhoAcademicoItem> itensOrdenados;
    private int posicao;

    public MaiorRiscoAcademicoIterator(List<RelatorioDesempenhoAcademicoItem> itens) {
        this.itensOrdenados = itens.stream()
                .sorted(Comparator
                        .comparingInt((RelatorioDesempenhoAcademicoItem item) -> pesoRisco(item.nivelRisco()))
                        .reversed()
                        .thenComparingDouble(RelatorioDesempenhoAcademicoItem::mediaGeral))
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

    private static int pesoRisco(String nivelRisco) {
        return switch (nivelRisco) {
            case "ALTO" -> 3;
            case "MODERADO" -> 2;
            default -> 1;
        };
    }
}
