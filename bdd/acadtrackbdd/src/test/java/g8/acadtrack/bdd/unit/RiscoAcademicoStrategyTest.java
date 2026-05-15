package g8.acadtrack.bdd.unit;

import g8.acadtrack.aplicacao.nota.risco.RiscoAltoStrategy;
import g8.acadtrack.aplicacao.nota.risco.RiscoModeradoStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RiscoAcademicoStrategyTest {

    private final RiscoAltoStrategy riscoAltoStrategy = new RiscoAltoStrategy();
    private final RiscoModeradoStrategy riscoModeradoStrategy = new RiscoModeradoStrategy();

    @Test
    void mediaAltaComDoisSimuladosRuinsNaoDeveSerRiscoAlto() {
        assertFalse(riscoAltoStrategy.aplica(9.0, 2));
    }

    @Test
    void mediaAltaComUmSimuladoRuimNaoDeveSerRiscoModerado() {
        assertFalse(riscoModeradoStrategy.aplica(9.0, 1));
    }

    @Test
    void mediaBaixaComDoisSimuladosRuinsDeveSerRiscoAlto() {
        assertTrue(riscoAltoStrategy.aplica(4.5, 2));
    }

    @Test
    void mediaBaixaComUmSimuladoRuimDeveSerRiscoModerado() {
        assertTrue(riscoModeradoStrategy.aplica(5.5, 1));
    }
}
