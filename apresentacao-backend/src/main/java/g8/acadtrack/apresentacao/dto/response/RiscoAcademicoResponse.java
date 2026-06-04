package g8.acadtrack.apresentacao.dto.response;

import g8.acadtrack.aplicacao.nota.AnaliseDesempenhoAcademicoResultado;

public record RiscoAcademicoResponse(String nivelRisco, String alerta) {

    public static RiscoAcademicoResponse fromApplication(AnaliseDesempenhoAcademicoResultado resultado) {
        return new RiscoAcademicoResponse(
                resultado.nivelRisco() != null ? resultado.nivelRisco().name() : null,
                resultado.alerta()
        );
    }
}
