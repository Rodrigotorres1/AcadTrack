package g8.acadtrack.aplicacao.riscoacademico;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.aluno.SituacaoAcademica;
import g8.acadtrack.dominiousuarios.notificacao.NotificacaoResponsavel;
import g8.acadtrack.dominiousuarios.notificacao.NotificacaoResponsavelRepository;
import g8.acadtrack.dominiousuarios.notificacao.PrioridadeNotificacao;
import g8.acadtrack.dominiousuarios.notificacao.StatusNotificacao;
import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;
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
                .ifPresent(aluno -> {
                    boolean jaNotificado = notificacaoResponsavelRepository.existeNotificacaoNaoLidaPara(
                            event.alunoId(), aluno.getResponsavelId(), event.nivelRisco());
                    if (!jaNotificado) {
                        notificacaoResponsavelRepository.salvar(criarNotificacao(event, aluno));
                    }
                });
    }

    private NotificacaoResponsavel criarNotificacao(RiscoAcademicoEvent event, Aluno aluno) {
        return new NotificacaoResponsavel(
                null,
                event.alunoId(),
                aluno.getResponsavelId(),
                event.nivelRisco(),
                determinarPrioridade(event.nivelRisco()),
                montarMensagem(event, aluno.getNome()),
                LocalDateTime.now(),
                StatusNotificacao.NAO_LIDA
        );
    }

    private PrioridadeNotificacao determinarPrioridade(NivelRiscoAcademico nivelRisco) {
        return switch (nivelRisco) {
            case ALTO -> PrioridadeNotificacao.ALTA;
            case MODERADO, BAIXO -> PrioridadeNotificacao.MEDIA;
        };
    }

    private String montarMensagem(RiscoAcademicoEvent event, String nomeAluno) {
        if (event.situacaoAcademica() == SituacaoAcademica.RECUPERACAO) {
            return "O aluno " + nomeAluno
                    + " ficou em recuperação, com risco acadêmico " + event.nivelRisco()
                    + " e média geral " + event.mediaGeral();
        }

        return "O aluno " + nomeAluno
                + " apresentou risco acadêmico " + event.nivelRisco()
                + " com média geral " + event.mediaGeral();
    }
}
