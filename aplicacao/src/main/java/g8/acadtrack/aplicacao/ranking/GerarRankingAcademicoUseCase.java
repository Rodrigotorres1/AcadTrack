package g8.acadtrack.aplicacao.ranking;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.aluno.SituacaoAcademica;
import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GerarRankingAcademicoUseCase {

    private final AlunoRepository alunoRepository;
    private final OrdenarRankingAcademicoService ordenarRankingAcademicoService;

    public GerarRankingAcademicoUseCase(
            AlunoRepository alunoRepository,
            OrdenarRankingAcademicoService ordenarRankingAcademicoService
    ) {
        this.alunoRepository = alunoRepository;
        this.ordenarRankingAcademicoService = ordenarRankingAcademicoService;
    }

    public List<RankingAcademicoItem> executar(int limite) {
        return executar(limite, CriterioRankingAcademico.MEDIA_DESC);
    }

    public List<RankingAcademicoItem> executar(int limite, CriterioRankingAcademico criterio) {
        List<RankingAcademicoItem> ordenados = ordenarRankingAcademicoService.ordenar(montarItens(), criterio);
        RankingAcademicoIterator iterator = new ListaRankingAcademicoIterator(ordenados);
        List<RankingAcademicoItem> resultado = new ArrayList<>();
        int quantidadeMaxima = limite <= 0 ? ordenados.size() : Math.min(limite, ordenados.size());

        while (iterator.hasNext() && resultado.size() < quantidadeMaxima) {
            resultado.add(iterator.next());
        }

        return resultado;
    }

    public Optional<RankingAcademicoItem> buscarPosicaoAluno(Long alunoId) {
        return executar(0).stream()
                .filter(item -> item.alunoId().equals(alunoId))
                .findFirst();
    }

    private List<RankingAcademicoItem> montarItens() {
        return alunoRepository.buscarAlunosComNotas()
                .stream()
                .map(this::mapearAluno)
                .toList();
    }

    private RankingAcademicoItem mapearAluno(Aluno aluno) {
        SituacaoAcademica situacao = aluno.getSituacaoAcademica();
        return new RankingAcademicoItem(
                aluno.getId(),
                aluno.getNome(),
                aluno.getMediaAritmetica(),
                0,
                situacao.name(),
                nivelRiscoPorSituacao(situacao)
        );
    }

    private NivelRiscoAcademico nivelRiscoPorSituacao(SituacaoAcademica situacao) {
        return switch (situacao) {
            case REPROVADO -> NivelRiscoAcademico.ALTO;
            case RECUPERACAO -> NivelRiscoAcademico.MODERADO;
            case APROVADO -> NivelRiscoAcademico.BAIXO;
        };
    }
}
