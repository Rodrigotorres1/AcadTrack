package br.com.acadtrack.apresentacao.dto.response;

import br.com.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;

public class SimuladoDisciplinaResponse {

    private Long id;
    private Long simuladoId;
    private Long disciplinaId;
    private double peso;

    public SimuladoDisciplinaResponse(Long id, Long simuladoId, Long disciplinaId, double peso) {
        this.id = id;
        this.simuladoId = simuladoId;
        this.disciplinaId = disciplinaId;
        this.peso = peso;
    }

    public static SimuladoDisciplinaResponse fromDomain(SimuladoDisciplina simuladoDisciplina) {
        return new SimuladoDisciplinaResponse(
                simuladoDisciplina.getId(),
                simuladoDisciplina.getSimuladoId(),
                simuladoDisciplina.getDisciplinaId(),
                simuladoDisciplina.getPeso()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getSimuladoId() {
        return simuladoId;
    }

    public Long getDisciplinaId() {
        return disciplinaId;
    }

    public double getPeso() {
        return peso;
    }
}
