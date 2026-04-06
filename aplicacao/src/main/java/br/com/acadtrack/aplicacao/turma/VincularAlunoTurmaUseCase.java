package br.com.acadtrack.aplicacao.turma;

import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.aluno.AlunoRepository;
import br.com.acadtrack.dominioacademico.turma.Turma;
import br.com.acadtrack.dominioacademico.turma.TurmaRepository;
import org.springframework.stereotype.Service;

@Service
public class VincularAlunoTurmaUseCase {

    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;

    public VincularAlunoTurmaUseCase(AlunoRepository alunoRepository, TurmaRepository turmaRepository) {
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
    }

    public Aluno executar(Long alunoId, Long turmaId) {
        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

        Turma turma = turmaRepository.buscarPorId(turmaId)
                .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada"));

        aluno.vincularTurma(turma.getId());

        return alunoRepository.salvar(aluno);
    }
}