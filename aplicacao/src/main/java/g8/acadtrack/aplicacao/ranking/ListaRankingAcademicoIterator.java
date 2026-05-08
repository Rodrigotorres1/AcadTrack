package g8.acadtrack.aplicacao.ranking;

import java.util.List;
import java.util.NoSuchElementException;

public class ListaRankingAcademicoIterator implements RankingAcademicoIterator {

    private final List<RankingAcademicoItem> itens;
    private int indiceAtual;

    public ListaRankingAcademicoIterator(List<RankingAcademicoItem> itens) {
        this.itens = List.copyOf(itens);
    }

    @Override
    public boolean hasNext() {
        return indiceAtual < itens.size();
    }

    @Override
    public RankingAcademicoItem next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Ranking academico sem proximo item");
        }

        return itens.get(indiceAtual++);
    }
}
