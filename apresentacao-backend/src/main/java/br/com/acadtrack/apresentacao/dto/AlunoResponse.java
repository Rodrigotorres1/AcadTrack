package br.com.acadtrack.apresentacao.dto;

import br.com.acadtrack.dominioacademico.aluno.Aluno;

public class AlunoResponse {

    private Long id;
    private String nome;
    private String email;
    private Long turmaId;
    private Long responsavelId;

    public AlunoResponse(Long id, String nome, String email, Long turmaId, Long responsavelId) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.turmaId = turmaId;
        this.responsavelId = responsavelId;
    }

    public static AlunoResponse fromDomain(Aluno aluno) {
        return new AlunoResponse(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getTurmaId(),
                aluno.getResponsavelId()
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

    public Long getTurmaId() {
        return turmaId;
    }

    public Long getResponsavelId() {
        return responsavelId;
    }
}