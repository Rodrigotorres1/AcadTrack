package br.com.acadtrack.dominiousuarios.responsavel;

public class Responsavel {

    private Long id;
    private String nome;
    private String email;

    public Responsavel(Long id, String nome, String email) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do responsável não pode ser vazio");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email do responsável não pode ser vazio");
        }

        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}