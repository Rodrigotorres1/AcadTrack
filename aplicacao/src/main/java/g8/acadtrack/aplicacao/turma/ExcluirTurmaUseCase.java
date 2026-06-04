package g8.acadtrack.aplicacao.turma;

import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.turma.TurmaRepository;
import g8.acadtrack.dominiocompartilhado.excecao.ConflitoDeEstadoException;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExcluirTurmaUseCase {

    private final TurmaRepository turmaRepository;
    private final AlunoRepository alunoRepository;

    public ExcluirTurmaUseCase(TurmaRepository turmaRepository, AlunoRepository alunoRepository) {
        this.turmaRepository = turmaRepository;
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public void executar(Long turmaId) {
        turmaRepository.buscarPorId(turmaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Turma não encontrada"));

        boolean temAlunos = alunoRepository.buscarTodos().stream()
                .anyMatch(a -> turmaId.equals(a.getTurmaId()));

        if (temAlunos) {
            throw new ConflitoDeEstadoException("Não é possível excluir turma com alunos matriculados");
        }

        turmaRepository.excluirPorId(turmaId);
    }
}
