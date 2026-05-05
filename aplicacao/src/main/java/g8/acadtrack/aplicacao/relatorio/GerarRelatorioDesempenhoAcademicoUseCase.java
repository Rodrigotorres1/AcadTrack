package g8.acadtrack.aplicacao.relatorio;

import g8.acadtrack.aplicacao.nota.AnaliseDesempenhoAcademicoResultado;
import g8.acadtrack.aplicacao.nota.AnalisarDesempenhoAcademicoUseCase;
import g8.acadtrack.aplicacao.planoestudo.PlanoEstudoRecomendadoResultado;
import g8.acadtrack.aplicacao.planoestudo.SelecionadorPlanoEstudoService;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GerarRelatorioDesempenhoAcademicoUseCase {

    private final AlunoRepository alunoRepository;
    private final NotaRepository notaRepository;
    private final AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase;
    private final SelecionadorPlanoEstudoService selecionadorPlanoEstudoService;

    public GerarRelatorioDesempenhoAcademicoUseCase(
            AlunoRepository alunoRepository,
            NotaRepository notaRepository,
            AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase,
            SelecionadorPlanoEstudoService selecionadorPlanoEstudoService
    ) {
        this.alunoRepository = alunoRepository;
        this.notaRepository = notaRepository;
        this.analisarDesempenhoAcademicoUseCase = analisarDesempenhoAcademicoUseCase;
        this.selecionadorPlanoEstudoService = selecionadorPlanoEstudoService;
    }

    public RelatorioDesempenhoAcademicoResultado executar(CriterioOrdenacaoRelatorio criterioOrdenacao) {
        CriterioOrdenacaoRelatorio criterio = CriterioOrdenacaoRelatorio.padraoSeNulo(criterioOrdenacao);
        RelatorioDesempenhoAcademico relatorio = new RelatorioDesempenhoAcademico(montarItensAnalisaveis());
        IteratorAcademico<RelatorioDesempenhoAcademicoItem> iterator = relatorio.iterator(criterio);

        List<RelatorioDesempenhoAcademicoItem> itensOrdenados = new ArrayList<>();
        while (iterator.temProximo()) {
            itensOrdenados.add(iterator.proximo());
        }

        return new RelatorioDesempenhoAcademicoResultado(
                criterio,
                relatorio.totalItens(),
                itensOrdenados
        );
    }

    private List<RelatorioDesempenhoAcademicoItem> montarItensAnalisaveis() {
        List<RelatorioDesempenhoAcademicoItem> itens = new ArrayList<>();

        for (Aluno aluno : alunoRepository.buscarTodos()) {
            if (notaRepository.buscarPorAlunoId(aluno.getId()).isEmpty()) {
                continue;
            }

            AnaliseDesempenhoAcademicoResultado analise = analisarDesempenhoAcademicoUseCase.executar(aluno.getId());
            PlanoEstudoRecomendadoResultado plano = selecionadorPlanoEstudoService.recomendar(analise);
            itens.add(new RelatorioDesempenhoAcademicoItem(
                    aluno.getId(),
                    aluno.getNome(),
                    analise.mediaGeral(),
                    analise.nivelRisco(),
                    aluno.getSituacaoAcademica(),
                    plano.tipoPlano()
            ));
        }

        return itens;
    }
}
