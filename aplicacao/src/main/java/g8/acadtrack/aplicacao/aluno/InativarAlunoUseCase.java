package g8.acadtrack.aplicacao.aluno;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

@Service
public class InativarAlunoUseCase {

    private final AlunoRepository alunoRepository;

    public InativarAlunoUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Aluno executar(Long alunoId) {
        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno nao encontrado."));
        aluno.inativar();
        return alunoRepository.salvar(aluno);
    }
}
