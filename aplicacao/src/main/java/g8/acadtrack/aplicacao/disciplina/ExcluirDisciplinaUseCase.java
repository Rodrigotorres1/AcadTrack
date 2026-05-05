package g8.acadtrack.aplicacao.disciplina;

import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

@Service
public class ExcluirDisciplinaUseCase {

    private final DisciplinaRepository disciplinaRepository;

    public ExcluirDisciplinaUseCase(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    public void executar(Long disciplinaId) {
        disciplinaRepository.buscarPorId(disciplinaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Disciplina não encontrada"));

        if (disciplinaRepository.possuiVinculoAcademico(disciplinaId)) {
            throw new IllegalStateException("Disciplina vinculada a registros acadêmicos não pode ser excluída");
        }

        disciplinaRepository.excluirPorId(disciplinaId);
    }
}
