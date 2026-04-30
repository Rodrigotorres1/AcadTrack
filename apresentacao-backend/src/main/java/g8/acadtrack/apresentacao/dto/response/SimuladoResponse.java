package g8.acadtrack.apresentacao.dto.response;

import g8.acadtrack.dominioavaliacao.simulado.Simulado;

public class SimuladoResponse {

    private Long id;
    private String descricao;

    public SimuladoResponse(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public static SimuladoResponse fromDomain(Simulado simulado) {
        return new SimuladoResponse(
                simulado.getId(),
                simulado.getDescricao()
        );
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }
}