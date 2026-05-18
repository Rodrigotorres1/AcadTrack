package g8.acadtrack.bdd.unit;

import g8.acadtrack.aplicacao.ranking.CriterioRankingAcademico;
import g8.acadtrack.aplicacao.ranking.OrdenarRankingAcademicoService;
import g8.acadtrack.aplicacao.ranking.RankingAcademicoItem;
import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrdenarRankingAcademicoServiceTest {

    private final OrdenarRankingAcademicoService service = new OrdenarRankingAcademicoService();

    @Test
    void deveOrdenarPorMediaDecrescentePorPadrao() {
        List<RankingAcademicoItem> ranking = service.ordenar(
                List.of(
                        item(1L, "Ana", 7.0, NivelRiscoAcademico.BAIXO),
                        item(2L, "Bruno", 9.0, NivelRiscoAcademico.BAIXO),
                        item(3L, "Caio", 8.0, NivelRiscoAcademico.BAIXO)
                ),
                CriterioRankingAcademico.MEDIA_DESC
        );

        assertEquals(List.of(2L, 3L, 1L), ranking.stream().map(RankingAcademicoItem::alunoId).toList());
        assertEquals(List.of(1, 2, 3), ranking.stream().map(RankingAcademicoItem::posicao).toList());
    }

    @Test
    void deveOrdenarPorMediaCrescente() {
        List<RankingAcademicoItem> ranking = service.ordenar(
                List.of(
                        item(1L, "Ana", 7.0, NivelRiscoAcademico.BAIXO),
                        item(2L, "Bruno", 9.0, NivelRiscoAcademico.BAIXO),
                        item(3L, "Caio", 8.0, NivelRiscoAcademico.BAIXO)
                ),
                CriterioRankingAcademico.MEDIA_ASC
        );

        assertEquals(List.of(1L, 3L, 2L), ranking.stream().map(RankingAcademicoItem::alunoId).toList());
    }

    @Test
    void deveUsarNomeEIdComoDesempateEmMediaIgual() {
        List<RankingAcademicoItem> ranking = service.ordenar(
                List.of(
                        item(3L, "Bruno", 8.0, NivelRiscoAcademico.BAIXO),
                        item(2L, "Ana", 8.0, NivelRiscoAcademico.BAIXO),
                        item(1L, "Ana", 8.0, NivelRiscoAcademico.BAIXO)
                ),
                CriterioRankingAcademico.MEDIA_DESC
        );

        assertEquals(List.of(1L, 2L, 3L), ranking.stream().map(RankingAcademicoItem::alunoId).toList());
    }

    @Test
    void deveOrdenarPorRiscoDepoisMediaNomeEId() {
        List<RankingAcademicoItem> ranking = service.ordenar(
                List.of(
                        item(1L, "Ana", 9.0, NivelRiscoAcademico.BAIXO),
                        item(2L, "Bruno", 8.0, NivelRiscoAcademico.ALTO),
                        item(3L, "Caio", 7.0, NivelRiscoAcademico.MODERADO),
                        item(4L, "Davi", 6.0, NivelRiscoAcademico.ALTO)
                ),
                CriterioRankingAcademico.RISCO
        );

        assertEquals(List.of(4L, 2L, 3L, 1L), ranking.stream().map(RankingAcademicoItem::alunoId).toList());
    }

    @Test
    void deveRetornarListaVaziaAoOrdenarListaVazia() {
        assertTrue(service.ordenar(List.of(), CriterioRankingAcademico.MEDIA_DESC).isEmpty());
    }

    @Test
    void deveRetornarNovaListaComPosicoesSemAlterarItensOriginais() {
        RankingAcademicoItem item = item(1L, "Ana", 8.0, NivelRiscoAcademico.BAIXO);

        List<RankingAcademicoItem> ranking = service.ordenar(List.of(item), CriterioRankingAcademico.MEDIA_DESC);

        assertEquals(0, item.posicao());
        assertEquals(1, ranking.get(0).posicao());
    }

    private RankingAcademicoItem item(Long alunoId, String nome, double media, NivelRiscoAcademico nivelRisco) {
        return new RankingAcademicoItem(alunoId, nome, media, 0, "APROVADO", nivelRisco);
    }
}
