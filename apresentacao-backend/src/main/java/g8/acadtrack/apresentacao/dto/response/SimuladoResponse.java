package g8.acadtrack.apresentacao.dto.response;

import g8.acadtrack.aplicacao.simulado.SimuladoResumoResultado;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;

public class SimuladoResponse {

    private Long id;
    private String descricao;
    private Integer quantidadeDisciplinas;
    private boolean consistente;
    private String status;
    private String motivoInconsistencia;

    public SimuladoResponse(Long id, String descricao) {
        this(id, descricao, null, true, "Consistente", null);
    }

    public SimuladoResponse(
            Long id,
            String descricao,
            Integer quantidadeDisciplinas,
            boolean consistente,
            String status,
            String motivoInconsistencia
    ) {
        this.id = id;
        this.descricao = descricao;
        this.quantidadeDisciplinas = quantidadeDisciplinas;
        this.consistente = consistente;
        this.status = status;
        this.motivoInconsistencia = motivoInconsistencia;
    }

    public static SimuladoResponse fromDomain(Simulado simulado) {
        return new SimuladoResponse(
                simulado.getId(),
                simulado.getDescricao()
        );
    }

    public static SimuladoResponse fromResumo(SimuladoResumoResultado simulado) {
        return new SimuladoResponse(
                simulado.id(),
                simulado.descricao(),
                simulado.quantidadeDisciplinas(),
                simulado.consistente(),
                simulado.statusConsistencia(),
                simulado.motivoInconsistencia()
        );
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getQuantidadeDisciplinas() {
        return quantidadeDisciplinas;
    }

    public boolean isConsistente() {
        return consistente;
    }

    public String getStatus() {
        return status;
    }

    public String getMotivoInconsistencia() {
        return motivoInconsistencia;
    }
}
