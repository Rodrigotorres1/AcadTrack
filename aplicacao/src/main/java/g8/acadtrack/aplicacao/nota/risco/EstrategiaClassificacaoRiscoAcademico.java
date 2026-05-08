package g8.acadtrack.aplicacao.nota.risco;

public interface EstrategiaClassificacaoRiscoAcademico {

    boolean aplica(double mediaGeral, long simuladosComBaixoDesempenho);

    String nivel();
}
