package br.com.acadtrack.apresentacao.dto;

import br.com.acadtrack.dominioavaliacao.nota.Nota;

public class NotaResponse {

    private Long id;
    private Long alunoId;
    private Long simuladoId;
    private String disciplina;
    private double valor;

    public NotaResponse(Long id, Long alunoId, Long simuladoId, String disciplina, double valor) {
        this.id = id;
        this.alunoId = alunoId;
        this.simuladoId = simuladoId;
        this.disciplina = disciplina;
        this.valor = valor;
    }

    public static NotaResponse fromDomain(Nota nota) {
        return new NotaResponse(
                nota.getId(),
                nota.getAlunoId(),
                nota.getSimuladoId(),
                nota.getDisciplina(),
                nota.getValor()
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

    public String getDisciplina() {
        return disciplina;
    }

    public double getValor() {
        return valor;
    }
}