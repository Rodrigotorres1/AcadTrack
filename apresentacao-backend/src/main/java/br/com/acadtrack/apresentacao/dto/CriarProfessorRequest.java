package br.com.acadtrack.apresentacao.dto;

public class CriarProfessorRequest {

    private Long id;
    private String nome;
    private String email;

    public CriarProfessorRequest() {
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