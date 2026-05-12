package g8.acadtrack.aplicacao.nota.validacao;

import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;

public class ValidadorAlunoAtivoDecorator extends ValidadorLancamentoNotaDecorator {

    public ValidadorAlunoAtivoDecorator(ValidadorLancamentoNota proximo) {
        super(proximo);
    }

    @Override
    public void validar(DadosLancamentoNota dados) {
        if (dados.aluno() == null) {
            throw new EntidadeNaoEncontradaException("Aluno não encontrado");
        }

        if (!dados.aluno().isAtivo()) {
            throw new RegraDeNegocioException("Aluno inativo não pode receber lançamento de nota");
        }

        super.validar(dados);
    }
}
