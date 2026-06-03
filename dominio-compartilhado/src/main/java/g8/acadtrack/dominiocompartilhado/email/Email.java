package g8.acadtrack.dominiocompartilhado.email;

import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public final class Email {

    private static final Pattern FORMATO_EMAIL = Pattern.compile("^[^\\s@]+@(?:[^\\s@.]+\\.)+[^\\s@.]{2,}$");

    private final String endereco;

    public Email(String email) {
        if (email == null || email.isBlank()) {
            throw new RegraDeNegocioException("Email é obrigatório");
        }
        String normalizado = email.trim().toLowerCase(Locale.ROOT);
        if (!FORMATO_EMAIL.matcher(normalizado).matches()) {
            throw new RegraDeNegocioException("Email inválido");
        }
        this.endereco = normalizado;
    }

    public String getEndereco() {
        return endereco;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email other)) return false;
        return endereco.equals(other.endereco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endereco);
    }

    @Override
    public String toString() {
        return endereco;
    }
}
