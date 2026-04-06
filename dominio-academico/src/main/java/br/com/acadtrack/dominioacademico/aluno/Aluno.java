package br.com.acadtrack.dominioacademico.aluno;

public class Aluno {

    private Long id;
    private String nome;
    private String email;
    private Long turmaId;
    private Long responsavelId;

    public Aluno(Long id, String nome, String email, Long turmaId, Long responsavelId) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.turmaId = turmaId;
        this.responsavelId = responsavelId;
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

    public void vincularTurma(Long turmaId) {
        this.turmaId = turmaId;
    }

    public void vincularResponsavel(Long responsavelId) {
        this.responsavelId = responsavelId;
    }

    public void desvincularResponsavel() {
        this.responsavelId = null;
    }
    public void vincularResponsavelId(Long responsavelId){
        this.responsavelId = responsavelId;
    }
}