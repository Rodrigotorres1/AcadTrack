package g8.acadtrack.aplicacao.nota;

import g8.acadtrack.dominioavaliacao.nota.Nota;

public record NotaEnriquecida(Nota nota, String nomeDisciplina, String descricaoSimulado) {
}
