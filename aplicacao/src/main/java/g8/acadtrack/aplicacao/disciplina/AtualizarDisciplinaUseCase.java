package g8.acadtrack.aplicacao.disciplina;

import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominiocompartilhado.excecao.ConflitoDeEstadoException;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

@Service
public class AtualizarDisciplinaUseCase {

    private final DisciplinaRepository disciplinaRepository;

    public AtualizarDisciplinaUseCase(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    public Disciplina executar(Long disciplinaId, String novoNome) {
        Disciplina disciplina = disciplinaRepository.buscarPorId(disciplinaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Disciplina não encontrada"));

        String nomeNormalizado = Disciplina.normalizarNome(novoNome);
        if (!nomeNormalizado.equalsIgnoreCase(disciplina.getNome())) {
            disciplinaRepository.buscarPorNomeNormalizado(nomeNormalizado)
                    .ifPresent(outra -> {
                        throw new ConflitoDeEstadoException("Já existe uma disciplina com este nome");
                    });
        }

        disciplina.renomear(novoNome);
        return disciplinaRepository.salvar(disciplina);
    }
}
