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
        List<MediaSimuladoResponse> historicoSimulados
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
                resultado.historicoSimulados().stream()
                        .map(MediaSimuladoResponse::fromApplication)
                        .toList()
        );
    }

    public record MediaSimuladoResponse(
            Long simuladoId,
            double mediaPonderada,
            boolean baixoDesempenho
    ) {
        public static MediaSimuladoResponse fromApplication(AnaliseDesempenhoAcademicoResultado.MediaSimulado media) {
            return new MediaSimuladoResponse(
                    media.simuladoId(),
                    media.mediaPonderada(),
                    media.baixoDesempenho()
            );
        }
    }
}
