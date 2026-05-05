package g8.acadtrack.aplicacao.riscoacademico;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominiousuarios.notificacao.NotificacaoResponsavel;
import g8.acadtrack.dominiousuarios.notificacao.NotificacaoResponsavelRepository;
import g8.acadtrack.dominiousuarios.notificacao.PrioridadeNotificacao;
import g8.acadtrack.dominiousuarios.notificacao.StatusNotificacao;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificarResponsavelRiscoAcademicoObserver implements ObservadorRiscoAcademico {

    private final AlunoRepository alunoRepository;
    private final NotificacaoResponsavelRepository notificacaoResponsavelRepository;

    public NotificarResponsavelRiscoAcademicoObserver(
            AlunoRepository alunoRepository,
            NotificacaoResponsavelRepository notificacaoResponsavelRepository
    ) {
        this.alunoRepository = alunoRepository;
        this.notificacaoResponsavelRepository = notificacaoResponsavelRepository;
    }

    @Override
    public void aoIdentificarRisco(RiscoAcademicoEvent event) {
        alunoRepository.buscarPorId(event.alunoId())
                .filter(aluno -> aluno.getResponsavelId() != null)
                .filter(Aluno::isVinculoResponsavelAtivo)
                .ifPresent(aluno -> notificacaoResponsavelRepository.salvar(criarNotificacao(event, aluno.getResponsavelId())));
    }

    private NotificacaoResponsavel criarNotificacao(RiscoAcademicoEvent event, Long responsavelId) {
        return new NotificacaoResponsavel(
                null,
                event.alunoId(),
                responsavelId,
                event.nivelRisco(),
                determinarPrioridade(event.nivelRisco()),
                montarMensagem(event),
                LocalDateTime.now(),
                StatusNotificacao.NAO_LIDA
        );
    }

    private PrioridadeNotificacao determinarPrioridade(String nivelRisco) {
        if ("ALTO".equals(nivelRisco)) {
            return PrioridadeNotificacao.ALTA;
        }
        return PrioridadeNotificacao.MEDIA;
    }

    private String montarMensagem(RiscoAcademicoEvent event) {
        return "Aluno " + event.alunoId()
                + " apresentou risco academico " + event.nivelRisco()
                + " com media geral " + event.mediaGeral() + ".";
    }
}
