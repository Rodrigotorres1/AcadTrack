package br.com.acadtrack.dominiocompartilhado.validacao;

public class ValidadorTexto {

    private ValidadorTexto() {
    }

    public static void validarObrigatorio(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
    }
}