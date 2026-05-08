package g8.acadtrack.aplicacao.nota.validacao;

public class ValidadorValorNotaDecorator extends ValidadorLancamentoNotaDecorator {

    public ValidadorValorNotaDecorator(ValidadorLancamentoNota proximo) {
        super(proximo);
    }

    @Override
    public void validar(DadosLancamentoNota dados) {
        if (dados.valor() < 0.0 || dados.valor() > 10.0) {
            throw new IllegalArgumentException("Nota deve estar entre 0 e 10");
        }

        super.validar(dados);
    }
}
