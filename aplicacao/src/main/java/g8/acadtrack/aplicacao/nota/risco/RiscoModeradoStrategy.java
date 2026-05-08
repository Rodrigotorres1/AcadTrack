package g8.acadtrack.aplicacao.nota.risco;

public class RiscoModeradoStrategy implements EstrategiaClassificacaoRiscoAcademico {

    private static final double LIMIAR_MEDIA_RISCO = 6.0;

    @Override
    public boolean aplica(double mediaGeral, long simuladosComBaixoDesempenho) {
        return mediaGeral < LIMIAR_MEDIA_RISCO || simuladosComBaixoDesempenho == 1;
    }

    @Override
    public String nivel() {
        return "MODERADO";
    }
}
