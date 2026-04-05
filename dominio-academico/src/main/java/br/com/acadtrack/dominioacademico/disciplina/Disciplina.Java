package br.com.acadtrack.dominioacademico.disciplina;

public class Disciplina {

    private Long id;
    private String nome;

    public Disciplina(Long id, String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da disciplina é obrigatório");
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