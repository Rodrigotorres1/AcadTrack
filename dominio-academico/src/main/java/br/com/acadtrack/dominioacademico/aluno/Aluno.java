package br.com.acadtrack.dominioacademico.aluno;

public class Aluno {

    private Long id;
    private String nome;
    private String email;
    private Long turmaId;

    public Aluno(Long id, String nome, String email) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do aluno não pode ser vazio");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email do aluno não pode ser vazio");
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

    public Long getTurmaId() {
        return turmaId;
    }

    public void alterarNome(String novoNome) {
        if (novoNome == null || novoNome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido");
        }
        this.nome = novoNome;
    }

    public void vincularTurma(Long turmaId) {
        if (turmaId == null) {
            throw new IllegalArgumentException("Turma inválida");
        }
        this.turmaId = turmaId;
    }   
}