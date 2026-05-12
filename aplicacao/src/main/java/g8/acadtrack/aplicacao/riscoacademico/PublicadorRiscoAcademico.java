package g8.acadtrack.aplicacao.riscoacademico;

import g8.acadtrack.aplicacao.nota.AnaliseDesempenhoAcademicoResultado;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicadorRiscoAcademico {

    private final List<ObservadorRiscoAcademico> observadores;

    public PublicadorRiscoAcademico(List<ObservadorRiscoAcademico> observadores) {
        this.observadores = observadores;
    }

    public void publicar(RiscoAcademicoEvent event) {
        observadores.forEach(observador -> observador.aoIdentificarRisco(event));
    }

    public void publicarSeRiscoNotificavel(AnaliseDesempenhoAcademicoResultado analise) {
        if (!analise.riscoAcademico()) {
            return;
        }

        publicar(new RiscoAcademicoEvent(
                analise.alunoId(),
                analise.mediaGeral(),
                analise.nivelRisco(),
                analise.situacaoAcademica(),
                analise.posicaoRanking(),
                analise.alunoNoTop10()
        ));
    }
}
