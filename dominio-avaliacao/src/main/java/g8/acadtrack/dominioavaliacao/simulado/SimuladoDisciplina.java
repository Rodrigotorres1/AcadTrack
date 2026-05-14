package g8.acadtrack.dominioavaliacao.simulado;

import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;

public class SimuladoDisciplina {

    private Long id;
    private Long simuladoId;
    private Long disciplinaId;
    private double peso;

    public SimuladoDisciplina(Long id, Long simuladoId, Long disciplinaId, double peso) {
        if (simuladoId == null) {
            throw new RegraDeNegocioException("Simulado é obrigatório");
        }

        if (disciplinaId == null) {
            throw new RegraDeNegocioException("Disciplina é obrigatória");
        }

        if (peso <= 0) {
            throw new RegraDeNegocioException("Peso deve ser maior que zero");
        }

        this.id = id;
        this.simuladoId = simuladoId;
        this.disciplinaId = disciplinaId;
        this.peso = peso;
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
