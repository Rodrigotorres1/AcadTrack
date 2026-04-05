package br.com.acadtrack.dominiousuarios.professor;

public class Professor {

    private Long id;
    private String nome;
    private String email;

    public Professor(Long id, String nome, String email) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do professor não pode ser vazio");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email do professor não pode ser vazio");
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

    public void alterarNome(String novoNome) {
        if (novoNome == null || novoNome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido");
        }
        this.nome = novoNome;
    }
}