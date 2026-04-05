package br.com.acadtrack.apresentacao.dto;

import br.com.acadtrack.dominioacademico.aluno.Aluno;

public class AlunoResponse {

    private Long id;
    private String nome;
    private String email;

    public AlunoResponse(Long id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public static AlunoResponse fromDomain(Aluno aluno) {
        return new AlunoResponse(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail()
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