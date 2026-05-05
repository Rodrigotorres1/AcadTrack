package g8.acadtrack.aplicacao.planoestudo;

import g8.acadtrack.aplicacao.nota.AnaliseDesempenhoAcademicoResultado;
import g8.acadtrack.aplicacao.nota.AnalisarDesempenhoAcademicoUseCase;
import org.springframework.stereotype.Service;

@Service
public class RecomendarPlanoEstudoUseCase {

    private final AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase;
    private final SelecionadorPlanoEstudoService selecionadorPlanoEstudoService;

    public RecomendarPlanoEstudoUseCase(
            AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase,
            SelecionadorPlanoEstudoService selecionadorPlanoEstudoService
    ) {
        this.analisarDesempenhoAcademicoUseCase = analisarDesempenhoAcademicoUseCase;
        this.selecionadorPlanoEstudoService = selecionadorPlanoEstudoService;
    }

    public PlanoEstudoRecomendadoResultado executar(Long alunoId) {
        AnaliseDesempenhoAcademicoResultado analise = analisarDesempenhoAcademicoUseCase.executar(alunoId);
        return selecionadorPlanoEstudoService.recomendar(analise);
    }
}
