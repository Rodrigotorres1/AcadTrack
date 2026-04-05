package br.com.acadtrack.apresentacao.dto;

public class CriarProfessorRequest {

    private String nome;
    private String email;

    public CriarProfessorRequest() {
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}