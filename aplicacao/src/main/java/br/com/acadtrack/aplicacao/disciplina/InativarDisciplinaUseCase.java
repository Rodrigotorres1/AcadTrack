package br.com.acadtrack.aplicacao.disciplina;

import br.com.acadtrack.dominioacademico.disciplina.Disciplina;
import br.com.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import br.com.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

@Service
public class InativarDisciplinaUseCase {

    private final DisciplinaRepository disciplinaRepository;

    public InativarDisciplinaUseCase(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    public Disciplina executar(Long disciplinaId) {
        Disciplina disciplina = disciplinaRepository.buscarPorId(disciplinaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Disciplina não encontrada"));
        disciplina.inativar();
        return disciplinaRepository.salvar(disciplina);
    }
}
