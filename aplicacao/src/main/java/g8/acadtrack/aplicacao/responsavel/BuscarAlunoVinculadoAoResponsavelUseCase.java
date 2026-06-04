package g8.acadtrack.aplicacao.responsavel;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import g8.acadtrack.dominiousuarios.responsavel.ResponsavelRepository;
import org.springframework.stereotype.Service;

@Service
public class BuscarAlunoVinculadoAoResponsavelUseCase {

    private final AlunoRepository alunoRepository;
    private final ResponsavelRepository responsavelRepository;

    public BuscarAlunoVinculadoAoResponsavelUseCase(
            AlunoRepository alunoRepository,
            ResponsavelRepository responsavelRepository
    ) {
        this.alunoRepository = alunoRepository;
        this.responsavelRepository = responsavelRepository;
    }

    public Aluno executar(Long responsavelId) {
        responsavelRepository.buscarPorId(responsavelId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Responsável não encontrado"));
        return alunoRepository.buscarPorResponsavelId(responsavelId)
                .stream()
                .filter(Aluno::isVinculoResponsavelAtivo)
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Nenhum aluno com vínculo ativo encontrado para este responsável"));
    }
}
