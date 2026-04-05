package br.com.acadtrack.dominiocompartilhado.identificador;

public class SimuladoId {

    private final Long valor;

    public SimuladoId(Long valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Identificador do simulado é obrigatório");
        }
        this.valor = valor;
    }

    public Long getValor() {
        return valor;
    }
}