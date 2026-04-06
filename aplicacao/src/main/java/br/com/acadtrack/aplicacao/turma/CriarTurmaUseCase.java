package br.com.acadtrack.aplicacao.turma;

import br.com.acadtrack.dominioacademico.turma.Turma;
import br.com.acadtrack.dominioacademico.turma.TurmaRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarTurmaUseCase {

    private final TurmaRepository turmaRepository;

    public CriarTurmaUseCase(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }

    public Turma executar(String nome) {
        Turma turma = new Turma(null, nome);
        return turmaRepository.salvar(turma);
    }
}