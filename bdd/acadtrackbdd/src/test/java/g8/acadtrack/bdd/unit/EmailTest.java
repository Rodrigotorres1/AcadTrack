package g8.acadtrack.bdd.unit;

import g8.acadtrack.dominiocompartilhado.email.Email;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailTest {

    @Test
    void deveNormalizarEmailParaLowercase() {
        assertEquals("alice@email.com", new Email("Alice@Email.com").getEndereco());
    }

    @Test
    void deveRemoverEspacosExtrasAoNormalizarEmail() {
        assertEquals("alice@email.com", new Email("  alice@email.com  ").getEndereco());
    }

    @Test
    void deveGerarMesmoValorParaEmailsEquivalentesComCapitalizacaoDiferente() {
        assertEquals(
                new Email("Alice@Email.com").getEndereco(),
                new Email(" alice@email.com ").getEndereco()
        );
    }
}
