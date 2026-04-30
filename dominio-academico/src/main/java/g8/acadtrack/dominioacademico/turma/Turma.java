package g8.acadtrack.dominioacademico.turma;

public class Turma {

    private Long id;
    private String nome;

    public Turma(Long id, String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da turma não pode ser vazio");
        }

        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}