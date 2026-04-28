package br.com.acadtrack.apresentacao.dto.response;

import br.com.acadtrack.dominioacademico.disciplina.Disciplina;

public class DisciplinaResponse {

    private Long id;
    private String nome;
    private String status;

    public DisciplinaResponse(Long id, String nome, String status) {
        this.id = id;
        this.nome = nome;
        this.status = status;
    }

    public static DisciplinaResponse fromDomain(Disciplina disciplina) {
        return new DisciplinaResponse(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getStatus().name()
        );
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getStatus() {
        return status;
    }
}
