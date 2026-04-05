package br.com.acadtrack.dominioavaliacao.media;

public class PesoDisciplina {

    private final double valor;

    public PesoDisciplina(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Peso deve ser maior que zero");
        }
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }
}