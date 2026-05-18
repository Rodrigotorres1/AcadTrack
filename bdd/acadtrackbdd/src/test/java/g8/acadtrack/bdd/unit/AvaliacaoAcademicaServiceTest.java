package g8.acadtrack.bdd.unit;

import g8.acadtrack.aplicacao.nota.AvaliacaoAcademicaService;
import g8.acadtrack.aplicacao.nota.AvaliacaoAcademicaService.SimuladoDisciplinaKey;
import g8.acadtrack.dominioacademico.aluno.SituacaoAcademica;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AvaliacaoAcademicaServiceTest {

    private final AvaliacaoAcademicaService service = new AvaliacaoAcademicaService();

    @Test
    void deveCalcularMediaPonderadaComPesosIguais() {
        double media = service.calcularMediaPonderada(
                List.of(
                        nota(1L, 1L, 8.0),
                        nota(1L, 2L, 6.0)
                ),
                Map.of(
                        new SimuladoDisciplinaKey(1L, 1L), 1.0,
                        new SimuladoDisciplinaKey(1L, 2L), 1.0
                )
        );

        assertEquals(7.0, media);
    }

    @Test
    void deveCalcularMediaPonderadaComPesosDiferentes() {
        double media = service.calcularMediaPonderada(
                List.of(
                        nota(1L, 1L, 10.0),
                        nota(1L, 2L, 4.0)
                ),
                Map.of(
                        new SimuladoDisciplinaKey(1L, 1L), 2.0,
                        new SimuladoDisciplinaKey(1L, 2L), 1.0
                )
        );

        assertEquals(8.0, media);
    }

    @Test
    void deveArredondarMediaPonderadaParaDuasCasas() {
        double media = service.calcularMediaPonderada(
                List.of(
                        nota(1L, 1L, 10.0),
                        nota(1L, 2L, 0.0)
                ),
                Map.of(
                        new SimuladoDisciplinaKey(1L, 1L), 1.0,
                        new SimuladoDisciplinaKey(1L, 2L), 2.0
                )
        );

        assertEquals(3.33, media);
    }

    @Test
    void deveIgnorarNotaSemPesoConfigurado() {
        double media = service.calcularMediaPonderada(
                List.of(
                        nota(1L, 1L, 10.0),
                        nota(1L, 2L, 0.0)
                ),
                Map.of(new SimuladoDisciplinaKey(1L, 1L), 1.0)
        );

        assertEquals(10.0, media);
    }

    @Test
    void deveRetornarZeroParaDadosVaziosOuInvalidos() {
        assertEquals(0.0, service.calcularMediaPonderada(List.of(), Map.of()));
        assertEquals(0.0, service.calcularMediaPonderada(null, Map.of()));
        assertEquals(0.0, service.calcularMediaPonderada(List.of(nota(1L, 1L, 8.0)), null));
        assertEquals(0.0, service.calcularMediaPonderada(
                List.of(nota(1L, 1L, 8.0)),
                Map.of(new SimuladoDisciplinaKey(2L, 1L), 1.0)
        ));
    }

    @Test
    void deveCalcularMediaAritmeticaEArredondar() {
        assertEquals(6.67, service.calcularMediaAritmetica(List.of(
                nota(1L, 1L, 10.0),
                nota(1L, 2L, 5.0),
                nota(1L, 3L, 5.0)
        )));
    }

    @Test
    void deveClassificarSituacaoAcademicaPelosLimiares() {
        assertEquals(SituacaoAcademica.APROVADO, service.calcularSituacao(7.0));
        assertEquals(SituacaoAcademica.RECUPERACAO, service.calcularSituacao(5.0));
        assertEquals(SituacaoAcademica.REPROVADO, service.calcularSituacao(4.99));
    }

    @Test
    void deveIdentificarBaixoDesempenhoDeSimulado() {
        assertTrue(service.isBaixoDesempenhoSimulado(4.99));
        assertFalse(service.isBaixoDesempenhoSimulado(5.0));
    }

    private Nota nota(Long simuladoId, Long disciplinaId, double valor) {
        return new Nota(null, 1L, simuladoId, disciplinaId, valor);
    }
}
