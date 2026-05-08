package g8.acadtrack.dominiocompartilhado.excecao;

public class ConflitoDeEstadoException extends RuntimeException {

    public ConflitoDeEstadoException(String mensagem) {
        super(mensagem);
    }
}
