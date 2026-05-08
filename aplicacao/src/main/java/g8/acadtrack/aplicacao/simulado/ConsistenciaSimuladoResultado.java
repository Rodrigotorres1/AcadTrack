package g8.acadtrack.aplicacao.simulado;

public record ConsistenciaSimuladoResultado(
        boolean consistente,
        String motivoInconsistencia
) {

    public String statusConsistencia() {
        return consistente ? "Consistente" : "Inconsistente";
    }
}
