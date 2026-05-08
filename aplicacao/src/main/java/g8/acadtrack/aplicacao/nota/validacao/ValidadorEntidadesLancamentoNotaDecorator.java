package g8.acadtrack.aplicacao.nota.validacao;

import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;

public class ValidadorEntidadesLancamentoNotaDecorator extends ValidadorLancamentoNotaDecorator {

    private final AlunoRepository alunoRepository;
    private final SimuladoRepository simuladoRepository;
    private final DisciplinaRepository disciplinaRepository;

    public ValidadorEntidadesLancamentoNotaDecorator(
            ValidadorLancamentoNota proximo,
            AlunoRepository alunoRepository,
            SimuladoRepository simuladoRepository,
            DisciplinaRepository disciplinaRepository
    ) {
        super(proximo);
        this.alunoRepository = alunoRepository;
        this.simuladoRepository = simuladoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    @Override
    public void validar(DadosLancamentoNota dados) {
        alunoRepository.buscarPorId(dados.alunoId())
                .orElseThrow(() -> new IllegalArgumentException("Aluno nao encontrado"));
        simuladoRepository.buscarPorId(dados.simuladoId())
                .orElseThrow(() -> new IllegalArgumentException("Simulado nao encontrado"));
        disciplinaRepository.buscarPorId(dados.disciplinaId())
                .orElseThrow(() -> new IllegalArgumentException("Disciplina nao encontrada"));

        super.validar(dados);
    }
}
