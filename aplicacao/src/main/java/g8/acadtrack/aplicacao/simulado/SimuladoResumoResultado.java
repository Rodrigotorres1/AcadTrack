package g8.acadtrack.aplicacao.simulado;

public record SimuladoResumoResultado(
        Long id,
        String descricao,
        int quantidadeDisciplinas,
        boolean consistente,
        String statusConsistencia,
        String motivoInconsistencia
) {
}
