package g8.acadtrack.dominioacademico.turma;

import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;

import java.util.Locale;

public class Turma {

    private Long id;
    private String nome;

    public Turma(Long id, String nome) {
        if (nome == null || nome.isBlank()) {
            throw new RegraDeNegocioException("Nome da turma não pode ser vazio");
        }

        this.id = id;
        this.nome = nome.trim();
    }

    public static String normalizarNome(String nome) {
        return String.valueOf(nome)
                .trim()
                .toLowerCase(Locale.ROOT)
                .replace("°", "º")
                .replaceAll("\\s+", "");
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getNomeNormalizado() {
        return normalizarNome(nome);
    }
}
