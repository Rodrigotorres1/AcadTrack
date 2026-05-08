package g8.acadtrack.aplicacao.simulado;

import java.util.List;

public record SimuladoDetalheResultado(
        Long id,
        String descricao,
        boolean consistente,
        String statusConsistencia,
        String motivoInconsistencia,
        List<DisciplinaVinculada> disciplinas,
        NotasRelacionadas notasRelacionadas,
        List<AlunoParticipante> alunosParticipantes
) {

    public record DisciplinaVinculada(
            Long id,
            String nome,
            String status
    ) {
    }

    public record NotasRelacionadas(
            int totalNotas,
            int alunosComNotas
    ) {
    }

    public record AlunoParticipante(
            Long alunoId,
            String nome,
            int quantidadeNotas,
            double media
    ) {
    }
}
