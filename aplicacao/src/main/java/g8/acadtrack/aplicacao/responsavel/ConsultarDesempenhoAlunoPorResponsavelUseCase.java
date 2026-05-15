package g8.acadtrack.aplicacao.responsavel;

import g8.acadtrack.aplicacao.nota.AnaliseDesempenhoAcademicoResultado;
import g8.acadtrack.aplicacao.nota.AnalisarDesempenhoAcademicoUseCase;
import g8.acadtrack.dominioacademico.aluno.PermissaoResponsavel;
import org.springframework.stereotype.Service;

@Service
public class ConsultarDesempenhoAlunoPorResponsavelUseCase {

    private final AcessoResponsavelAlunoService acessoResponsavelAlunoService;
    private final AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase;

    public ConsultarDesempenhoAlunoPorResponsavelUseCase(
            AcessoResponsavelAlunoService acessoResponsavelAlunoService,
            AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase
    ) {
        this.acessoResponsavelAlunoService = acessoResponsavelAlunoService;
        this.analisarDesempenhoAcademicoUseCase = analisarDesempenhoAcademicoUseCase;
    }

    public AnaliseDesempenhoAcademicoResultado executar(Long responsavelId, Long alunoId) {
        acessoResponsavelAlunoService.executar(alunoId, responsavelId, PermissaoResponsavel.VISUALIZAR_DESEMPENHO);
        // Consulta explícita: inclui posição no ranking para o responsável.
        // Fluxos de escrita usam análise leve de risco, sem montar desempenho nem ranking.
        return analisarDesempenhoAcademicoUseCase.executar(alunoId);
    }
}
