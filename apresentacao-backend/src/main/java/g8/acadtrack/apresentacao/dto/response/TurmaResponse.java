package g8.acadtrack.apresentacao.dto.response;

import g8.acadtrack.dominioacademico.turma.Turma;

public class TurmaResponse {

    private Long id;
    private String nome;

    public TurmaResponse(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public static TurmaResponse fromDomain(Turma turma) {
        return new TurmaResponse(
                turma.getId(),
                turma.getNome()
        );
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}