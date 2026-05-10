package g8.acadtrack.aplicacao.nota.validacao;

import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;

public class ValidadorValorNotaDecorator extends ValidadorLancamentoNotaDecorator {

    public ValidadorValorNotaDecorator(ValidadorLancamentoNota proximo) {
        super(proximo);
    }

    @Override
    public void validar(DadosLancamentoNota dados) {
        if (dados.valor() < 0.0 || dados.valor() > 10.0) {
            throw new RegraDeNegocioException("A nota deve estar entre 0 e 10");
        }

        super.validar(dados);
    }
}
