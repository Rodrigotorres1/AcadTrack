package g8.acadtrack.aplicacao.responsavel;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DesvincularResponsavelUseCase {

    private final AlunoRepository alunoRepository;

    public DesvincularResponsavelUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public Aluno executar(Long alunoId) {
        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno não encontrado"));

        aluno.desvincularResponsavel();
        return alunoRepository.salvar(aluno);
    }
}
