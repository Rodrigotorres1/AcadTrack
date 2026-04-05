package br.com.acadtrack.dominiocompartilhado.validacao;

public class ValidadorNumero {

    private ValidadorNumero() {
    }

    public static void validarPositivo(double valor, String mensagem) {
        if (valor <= 0) {
            throw new IllegalArgumentException(mensagem);
        }
    }
}