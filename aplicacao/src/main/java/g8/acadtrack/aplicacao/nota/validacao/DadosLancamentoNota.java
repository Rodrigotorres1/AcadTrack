package g8.acadtrack.aplicacao.nota.validacao;

public record DadosLancamentoNota(
        Long alunoId,
        Long simuladoId,
        Long disciplinaId,
        double valor
) {
}
