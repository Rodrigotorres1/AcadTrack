package g8.acadtrack.aplicacao.nota.validacao;

import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;

public class ValidadorDisciplinaAtivaDecorator extends ValidadorLancamentoNotaDecorator {

    private final DisciplinaRepository disciplinaRepository;

    public ValidadorDisciplinaAtivaDecorator(
            ValidadorLancamentoNota proximo,
            DisciplinaRepository disciplinaRepository
    ) {
        super(proximo);
        this.disciplinaRepository = disciplinaRepository;
    }

    @Override
    public void validar(DadosLancamentoNota dados) {
        boolean disciplinaAtiva = disciplinaRepository.buscarPorId(dados.disciplinaId())
                .map(disciplina -> disciplina.estaAtiva())
                .orElse(false);

        if (!disciplinaAtiva) {
            throw new IllegalStateException("Disciplina inativa nao pode receber lancamento de nota");
        }

        super.validar(dados);
    }
}
