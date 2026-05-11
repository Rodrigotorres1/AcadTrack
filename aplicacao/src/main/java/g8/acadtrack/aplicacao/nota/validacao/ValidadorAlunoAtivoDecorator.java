package g8.acadtrack.aplicacao.nota.validacao;

import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;

public class ValidadorAlunoAtivoDecorator extends ValidadorLancamentoNotaDecorator {

    private final AlunoRepository alunoRepository;

    public ValidadorAlunoAtivoDecorator(
            ValidadorLancamentoNota proximo,
            AlunoRepository alunoRepository
    ) {
        super(proximo);
        this.alunoRepository = alunoRepository;
    }

    @Override
    public void validar(DadosLancamentoNota dados) {
        boolean alunoAtivo = alunoRepository.buscarPorId(dados.alunoId())
                .map(aluno -> aluno.isAtivo())
                .orElse(false);

        if (!alunoAtivo) {
            throw new RegraDeNegocioException("Aluno inativo não pode receber lançamento de nota");
        }

        super.validar(dados);
    }
}
