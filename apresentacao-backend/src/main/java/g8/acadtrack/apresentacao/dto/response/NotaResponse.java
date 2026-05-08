package g8.acadtrack.apresentacao.dto.response;

import g8.acadtrack.dominioavaliacao.nota.Nota;

public class NotaResponse {

    private Long id;
    private Long alunoId;
    private Long simuladoId;
    private Long disciplinaId;
    private double valor;
    private String nomeDisciplina;
    private String descricaoSimulado;

    public NotaResponse(Long id, Long alunoId, Long simuladoId, Long disciplinaId, double valor,
                        String nomeDisciplina, String descricaoSimulado) {
        this.id = id;
        this.alunoId = alunoId;
        this.simuladoId = simuladoId;
        this.disciplinaId = disciplinaId;
        this.valor = valor;
        this.nomeDisciplina = nomeDisciplina;
        this.descricaoSimulado = descricaoSimulado;
    }

    public static NotaResponse fromDomain(Nota nota) {
        return new NotaResponse(
                nota.getId(),
                nota.getAlunoId(),
                nota.getSimuladoId(),
                nota.getDisciplinaId(),
                nota.getValor(),
                null,
                null
        );
    }

    public static NotaResponse fromDomain(Nota nota, String nomeDisciplina, String descricaoSimulado) {
        return new NotaResponse(
                nota.getId(),
                nota.getAlunoId(),
                nota.getSimuladoId(),
                nota.getDisciplinaId(),
                nota.getValor(),
                nomeDisciplina,
                descricaoSimulado
        );
    }

    public Long getId() {
        return id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public Long getSimuladoId() {
        return simuladoId;
    }

    public Long getDisciplinaId() {
        return disciplinaId;
    }

    public double getValor() {
        return valor;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public String getDescricaoSimulado() {
        return descricaoSimulado;
    }
}
