package g8.acadtrack.aplicacao.nota.validacao;

import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominiocompartilhado.excecao.ConflitoDeEstadoException;

public class ValidadorNotaDuplicadaDecorator extends ValidadorLancamentoNotaDecorator {

    private final NotaRepository notaRepository;

    public ValidadorNotaDuplicadaDecorator(
            ValidadorLancamentoNota proximo,
            NotaRepository notaRepository
    ) {
        super(proximo);
        this.notaRepository = notaRepository;
    }

    @Override
    public void validar(DadosLancamentoNota dados) {
        if (notaRepository.existePorAlunoSimuladoDisciplina(dados.alunoId(), dados.simuladoId(), dados.disciplinaId())) {
            throw new ConflitoDeEstadoException("Ja existe nota lancada para este aluno, simulado e disciplina");
        }

        super.validar(dados);
    }
}
