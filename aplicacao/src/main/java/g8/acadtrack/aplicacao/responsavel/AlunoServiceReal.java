package g8.acadtrack.aplicacao.responsavel;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.aluno.PermissaoResponsavel;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

@Service
public class AlunoServiceReal implements AcessoResponsavelAlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoServiceReal(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @Override
    public Aluno executar(Long alunoId, Long responsavelId, PermissaoResponsavel permissaoResponsavel) {
        return alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno não encontrado"));
    }
}
