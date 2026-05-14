package g8.acadtrack.dominiocompartilhado.excecao;

public class AcessoDenegadoException extends RuntimeException {

    public AcessoDenegadoException(String mensagem) {
        super(mensagem);
    }
}
