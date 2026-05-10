package g8.acadtrack.aplicacao.turma;

import g8.acadtrack.dominioacademico.turma.Turma;
import g8.acadtrack.dominioacademico.turma.TurmaRepository;
import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CriarTurmaUseCase {

    private final TurmaRepository turmaRepository;

    public CriarTurmaUseCase(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }

    @Transactional
    public Turma executar(String nome) {
        Turma turma = new Turma(null, nome);
        if (turmaRepository.existeComNomeNormalizado(turma.getNomeNormalizado())) {
            throw new RegraDeNegocioException("Ja existe uma turma cadastrada com esse nome.");
        }
        return turmaRepository.salvar(turma);
    }
}
