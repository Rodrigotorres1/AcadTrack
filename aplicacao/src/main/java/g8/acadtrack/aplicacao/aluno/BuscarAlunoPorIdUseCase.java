package g8.acadtrack.aplicacao.aluno;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

@Service
public class BuscarAlunoPorIdUseCase {

    private final AlunoRepository alunoRepository;

    public BuscarAlunoPorIdUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Aluno executar(Long id) {
        return alunoRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno não encontrado"));
    }
}
