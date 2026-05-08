package g8.acadtrack.aplicacao.turma;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.turma.Turma;
import g8.acadtrack.dominioacademico.turma.TurmaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class LimparTurmasDuplicadasUseCase {

    private final TurmaRepository turmaRepository;
    private final AlunoRepository alunoRepository;

    public LimparTurmasDuplicadasUseCase(TurmaRepository turmaRepository, AlunoRepository alunoRepository) {
        this.turmaRepository = turmaRepository;
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public int executar() {
        Map<String, Turma> turmasMantidas = new HashMap<>();
        List<Turma> turmas = turmaRepository.buscarTodos()
                .stream()
                .sorted(Comparator.comparing(Turma::getId))
                .toList();

        int removidas = 0;
        for (Turma turma : turmas) {
            String nomeNormalizado = turma.getNomeNormalizado();
            Turma turmaMantida = turmasMantidas.get(nomeNormalizado);

            if (turmaMantida == null) {
                turmasMantidas.put(nomeNormalizado, turma);
                continue;
            }

            moverAlunos(turma.getId(), turmaMantida.getId());
            turmaRepository.excluirPorId(turma.getId());
            removidas++;
        }

        return removidas;
    }

    private void moverAlunos(Long turmaDuplicadaId, Long turmaMantidaId) {
        alunoRepository.buscarTodos()
                .stream()
                .filter(aluno -> Objects.equals(aluno.getTurmaId(), turmaDuplicadaId))
                .forEach(aluno -> moverAluno(aluno, turmaMantidaId));
    }

    private void moverAluno(Aluno aluno, Long turmaMantidaId) {
        aluno.substituirTurma(turmaMantidaId);
        alunoRepository.salvar(aluno);
    }
}
