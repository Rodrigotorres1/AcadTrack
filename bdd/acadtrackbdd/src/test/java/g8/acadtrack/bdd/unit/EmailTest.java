package g8.acadtrack.bdd.unit;

import g8.acadtrack.dominiocompartilhado.email.Email;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailTest {

    @Test
    void deveNormalizarEmailParaLowercase() {
        assertEquals("alice@email.com", Email.normalizar("Alice@Email.com"));
    }

    @Test
    void deveRemoverEspacosExtrasAoNormalizarEmail() {
        assertEquals("alice@email.com", Email.normalizar("  alice@email.com  "));
    }

    @Test
    void deveGerarMesmoValorParaEmailsEquivalentesComCapitalizacaoDiferente() {
        assertEquals(
                Email.normalizar("Alice@Email.com"),
                Email.normalizar(" alice@email.com ")
        );
    }
}
