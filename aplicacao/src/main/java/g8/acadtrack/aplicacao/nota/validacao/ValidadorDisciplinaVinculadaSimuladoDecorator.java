package g8.acadtrack.aplicacao.nota.validacao;

import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;

public class ValidadorDisciplinaVinculadaSimuladoDecorator extends ValidadorLancamentoNotaDecorator {

    public ValidadorDisciplinaVinculadaSimuladoDecorator(ValidadorLancamentoNota proximo) {
        super(proximo);
    }

    @Override
    public void validar(DadosLancamentoNota dados) {
        if (dados.simulado() == null || !dados.simulado().possuiDisciplina(dados.disciplinaId())) {
            throw new RegraDeNegocioException("Disciplina não vinculada ao simulado");
        }

        super.validar(dados);
    }
}
