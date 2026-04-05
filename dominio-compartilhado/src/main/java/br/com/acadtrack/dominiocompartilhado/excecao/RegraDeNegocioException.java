package br.com.acadtrack.dominiocompartilhado.excecao;

public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}