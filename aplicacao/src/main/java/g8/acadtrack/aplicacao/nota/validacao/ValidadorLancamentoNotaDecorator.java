package g8.acadtrack.aplicacao.nota.validacao;

public abstract class ValidadorLancamentoNotaDecorator implements ValidadorLancamentoNota {

    private final ValidadorLancamentoNota proximo;

    protected ValidadorLancamentoNotaDecorator(ValidadorLancamentoNota proximo) {
        this.proximo = proximo;
    }

    @Override
    public void validar(DadosLancamentoNota dados) {
        proximo.validar(dados);
    }
}
