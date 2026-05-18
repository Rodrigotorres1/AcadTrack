package g8.acadtrack.dominiocompartilhado.email;

import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;

import java.util.Locale;
import java.util.regex.Pattern;

public final class Email {

    private static final Pattern FORMATO_EMAIL = Pattern.compile("^[^\\s@]+@(?:[^\\s@.]+\\.)+[^\\s@.]{2,}$");

    private Email() {
    }

    public static String normalizar(String email) {
        if (email == null || email.isBlank()) {
            throw new RegraDeNegocioException("Email é obrigatório");
        }

        String emailNormalizado = email.trim().toLowerCase(Locale.ROOT);
        if (!FORMATO_EMAIL.matcher(emailNormalizado).matches()) {
            throw new RegraDeNegocioException("Email inválido");
        }

        return emailNormalizado;
    }
}
