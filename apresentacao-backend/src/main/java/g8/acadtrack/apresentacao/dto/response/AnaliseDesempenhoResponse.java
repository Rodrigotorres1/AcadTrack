package g8.acadtrack.apresentacao.dto.response;

import g8.acadtrack.aplicacao.nota.AnaliseDesempenhoAcademicoResultado;

import java.util.List;

public record AnaliseDesempenhoResponse(
        Long alunoId,
        double mediaGeral,
        int totalNotas,
        int totalSimulados,
        long simuladosComBaixoDesempenho,
        boolean riscoAcademico,
        String nivelRisco,
        String alerta,
        String situacaoAcademica,
        Integer posicaoRanking,
        int totalAlunosRanking,
        boolean alunoNoTop10,
        String mensagemRanking,
        List<MediaSimuladoResponse> historicoSimulados,
        List<MediaDisciplinaResponse> notasPorDisciplina
) {
    public static AnaliseDesempenhoResponse fromApplication(AnaliseDesempenhoAcademicoResultado resultado) {
        return new AnaliseDesempenhoResponse(
                resultado.alunoId(),
                resultado.mediaGeral(),
                resultado.totalNotas(),
                resultado.totalSimulados(),
                resultado.simuladosComBaixoDesempenho(),
                resultado.riscoAcademico(),
                resultado.nivelRisco(),
                resultado.alerta(),
                resultado.situacaoAcademica(),
                resultado.posicaoRanking(),
                resultado.totalAlunosRanking(),
                resultado.alunoNoTop10(),
                resultado.mensagemRanking(),
                resultado.historicoSimulados().stream()
                        .map(MediaSimuladoResponse::fromApplication)
                        .toList(),
                resultado.notasPorDisciplina().stream()
                        .map(MediaDisciplinaResponse::fromApplication)
                        .toList()
        );
    }

    public record MediaSimuladoResponse(
            Long simuladoId,
            String nomeSimulado,
            int quantidadeNotas,
            double mediaPonderada,
            boolean baixoDesempenho
    ) {
        public static MediaSimuladoResponse fromApplication(AnaliseDesempenhoAcademicoResultado.MediaSimulado media) {
            return new MediaSimuladoResponse(
                    media.simuladoId(),
                    media.nomeSimulado(),
                    media.quantidadeNotas(),
                    media.mediaPonderada(),
                    media.baixoDesempenho()
            );
        }
    }

    public record MediaDisciplinaResponse(
            Long disciplinaId,
            String nomeDisciplina,
            double media,
            String status,
            String nivelRisco
    ) {
        public static MediaDisciplinaResponse fromApplication(AnaliseDesempenhoAcademicoResultado.MediaDisciplina media) {
            return new MediaDisciplinaResponse(
                    media.disciplinaId(),
                    media.nomeDisciplina(),
                    media.media(),
                    media.status(),
                    media.nivelRisco()
            );
        }
    }
}
