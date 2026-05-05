package g8.acadtrack.aplicacao.disciplina;

import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

@Service
public class BuscarDisciplinaUseCase {

    private final DisciplinaRepository disciplinaRepository;

    public BuscarDisciplinaUseCase(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    public Disciplina executar(Long disciplinaId) {
        return disciplinaRepository.buscarPorId(disciplinaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Disciplina não encontrada"));
    }
}
