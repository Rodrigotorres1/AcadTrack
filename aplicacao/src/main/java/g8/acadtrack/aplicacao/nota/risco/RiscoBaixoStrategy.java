package g8.acadtrack.aplicacao.nota.risco;

public class RiscoBaixoStrategy implements EstrategiaClassificacaoRiscoAcademico {

    @Override
    public boolean aplica(double mediaGeral, long simuladosComBaixoDesempenho) {
        return true;
    }

    @Override
    public String nivel() {
        return "BAIXO";
    }
}
