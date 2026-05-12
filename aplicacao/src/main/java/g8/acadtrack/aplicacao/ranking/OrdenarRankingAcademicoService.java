package g8.acadtrack.aplicacao.ranking;

import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class OrdenarRankingAcademicoService {

    public List<RankingAcademicoItem> ordenar(List<RankingAcademicoItem> itens, CriterioRankingAcademico criterio) {
        List<RankingAcademicoItem> ordenados = itens.stream()
                .sorted(comparador(criterio))
                .toList();

        return IntStream.range(0, ordenados.size())
                .mapToObj(indice -> ordenados.get(indice).comPosicao(indice + 1))
                .toList();
    }

    private Comparator<RankingAcademicoItem> comparador(CriterioRankingAcademico criterio) {
        Comparator<RankingAcademicoItem> porNome = Comparator.comparing(RankingAcademicoItem::nomeAluno, String.CASE_INSENSITIVE_ORDER);
        Comparator<RankingAcademicoItem> porId = Comparator.comparing(RankingAcademicoItem::alunoId);

        if (criterio == CriterioRankingAcademico.MEDIA_ASC) {
            return Comparator.comparingDouble(RankingAcademicoItem::media)
                    .thenComparing(porNome)
                    .thenComparing(porId);
        }

        if (criterio == CriterioRankingAcademico.RISCO) {
            return Comparator.comparingInt((RankingAcademicoItem item) -> pesoRisco(item.nivelRisco()))
                    .thenComparing(Comparator.comparingDouble(RankingAcademicoItem::media))
                    .thenComparing(porNome)
                    .thenComparing(porId);
        }

        return Comparator.comparingDouble(RankingAcademicoItem::media)
                .reversed()
                .thenComparing(porNome)
                .thenComparing(porId);
    }

    private int pesoRisco(NivelRiscoAcademico nivelRisco) {
        return switch (nivelRisco) {
            case ALTO -> 0;
            case MODERADO -> 1;
            case BAIXO -> 2;
        };
    }
}
