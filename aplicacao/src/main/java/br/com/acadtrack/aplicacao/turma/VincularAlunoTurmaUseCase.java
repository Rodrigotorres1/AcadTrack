package br.com.acadtrack.aplicacao.turma;

import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.aluno.AlunoRepository;
import br.com.acadtrack.dominioacademico.turma.Turma;
import br.com.acadtrack.dominioacademico.turma.TurmaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VincularAlunoTurmaUseCase {

    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;

    public VincularAlunoTurmaUseCase(AlunoRepository alunoRepository, TurmaRepository turmaRepository) {
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
    }

    public void executar(Long alunoId, Long turmaId) {
        Optional<Aluno> alunoOptional = alunoRepository.buscarPorId(alunoId);
        Optional<Turma> turmaOptional = turmaRepository.buscarPorId(turmaId);

        if (alunoOptional.isEmpty()) {
            throw new IllegalArgumentException("Aluno não encontrado");
        }

        if (turmaOptional.isEmpty()) {
            throw new IllegalArgumentException("Turma não encontrada");
        }

        Aluno aluno = alunoOptional.get();
        Turma turma = turmaOptional.get();

        aluno.vincularTurma(turma.getId());
        alunoRepository.salvar(aluno);
    }
}