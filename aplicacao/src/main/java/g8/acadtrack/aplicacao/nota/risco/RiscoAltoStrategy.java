package g8.acadtrack.aplicacao.nota.risco;

public class RiscoAltoStrategy implements EstrategiaClassificacaoRiscoAcademico {

    private static final double LIMIAR_RISCO_ALTO_MEDIA = 5.0;

    @Override
    public boolean aplica(double mediaGeral, long simuladosComBaixoDesempenho) {
        return mediaGeral < LIMIAR_RISCO_ALTO_MEDIA || simuladosComBaixoDesempenho >= 2;
    }

    @Override
    public String nivel() {
        return "ALTO";
    }
}
