package br.com.acadtrack.aplicacao.nota;

import java.util.List;

public record AnaliseDesempenhoAcademicoResultado(
        Long alunoId,
        double mediaGeral,
        int totalNotas,
        int totalSimulados,
        long simuladosComBaixoDesempenho,
        boolean riscoAcademico,
        String nivelRisco,
        String alerta,
        List<MediaSimulado> historicoSimulados
) {
    public record MediaSimulado(
            Long simuladoId,
            double mediaPonderada,
            boolean baixoDesempenho
    ) {
    }
}
