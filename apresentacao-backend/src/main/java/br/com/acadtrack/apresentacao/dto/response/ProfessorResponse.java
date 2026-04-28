package br.com.acadtrack.apresentacao.dto.response;

import br.com.acadtrack.dominiousuarios.professor.Professor;

public class ProfessorResponse {

    private Long id;
    private String nome;
    private String email;

    public ProfessorResponse(Long id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public static ProfessorResponse fromDomain(Professor professor) {
        return new ProfessorResponse(
                professor.getId(),
                professor.getNome(),
                professor.getEmail()
        );
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