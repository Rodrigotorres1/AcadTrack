package g8.acadtrack.aplicacao.responsavel;

import g8.acadtrack.aplicacao.nota.AnaliseDesempenhoAcademicoResultado;
import g8.acadtrack.aplicacao.nota.AnalisarDesempenhoAcademicoUseCase;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.AcessoDenegadoException;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ConsultarRiscoAlunoPorResponsavelUseCase {

    private final AlunoRepository alunoRepository;
    private final AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase;

    public ConsultarRiscoAlunoPorResponsavelUseCase(
            AlunoRepository alunoRepository,
            AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase
    ) {
        this.alunoRepository = alunoRepository;
        this.analisarDesempenhoAcademicoUseCase = analisarDesempenhoAcademicoUseCase;
    }

    public AnaliseDesempenhoAcademicoResultado executar(Long responsavelId, Long alunoId) {
        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno não encontrado"));

        if (!Objects.equals(aluno.getResponsavelId(), responsavelId) || !aluno.isVinculoResponsavelAtivo()) {
            throw new AcessoDenegadoException("Responsável sem vínculo ativo com o aluno");
        }

        return analisarDesempenhoAcademicoUseCase.executar(alunoId);
    }
}
