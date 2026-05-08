package g8.acadtrack.aplicacao.responsavel;

import g8.acadtrack.aplicacao.nota.AnaliseDesempenhoAcademicoResultado;
import g8.acadtrack.aplicacao.nota.AnalisarDesempenhoAcademicoUseCase;
import g8.acadtrack.dominioacademico.aluno.PermissaoResponsavel;
import org.springframework.stereotype.Service;

@Service
public class ConsultarDesempenhoAlunoPorResponsavelUseCase {

    private final AcessoResponsavelAlunoProxy acessoResponsavelAlunoProxy;
    private final AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase;

    public ConsultarDesempenhoAlunoPorResponsavelUseCase(
            AcessoResponsavelAlunoProxy acessoResponsavelAlunoProxy,
            AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase
    ) {
        this.acessoResponsavelAlunoProxy = acessoResponsavelAlunoProxy;
        this.analisarDesempenhoAcademicoUseCase = analisarDesempenhoAcademicoUseCase;
    }

    public AnaliseDesempenhoAcademicoResultado executar(Long responsavelId, Long alunoId) {
        acessoResponsavelAlunoProxy.executar(alunoId, responsavelId, PermissaoResponsavel.VISUALIZAR_DESEMPENHO);
        return analisarDesempenhoAcademicoUseCase.executar(alunoId);
    }
}
