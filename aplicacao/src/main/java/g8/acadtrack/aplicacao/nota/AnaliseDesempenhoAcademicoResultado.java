package g8.acadtrack.aplicacao.nota;

import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;

import java.util.List;

public record AnaliseDesempenhoAcademicoResultado(
        Long alunoId,
        double mediaGeral,
        int totalNotas,
        int totalSimulados,
        long simuladosComBaixoDesempenho,
        boolean riscoAcademico,
        NivelRiscoAcademico nivelRisco,
        String alerta,
        String situacaoAcademica,
        Integer posicaoRanking,
        int totalAlunosRanking,
        boolean alunoNoTop10,
        String mensagemRanking,
        List<MediaSimulado> historicoSimulados,
        List<MediaDisciplina> notasPorDisciplina
) {
    public record MediaSimulado(
            Long simuladoId,
            String nomeSimulado,
            int quantidadeNotas,
            double mediaPonderada,
            boolean baixoDesempenho
    ) {
    }

    public record MediaDisciplina(
            Long disciplinaId,
            String nomeDisciplina,
            double media,
            String status,
            NivelRiscoAcademico nivelRisco
    ) {
    }
}
