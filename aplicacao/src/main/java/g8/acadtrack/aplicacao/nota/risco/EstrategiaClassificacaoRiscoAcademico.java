package g8.acadtrack.aplicacao.nota.risco;

import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;

public interface EstrategiaClassificacaoRiscoAcademico {

    boolean aplica(double mediaGeral, long simuladosComBaixoDesempenho);

    NivelRiscoAcademico nivel();
}
