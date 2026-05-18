package g8.acadtrack.dominioacademico.aluno.evento;

import g8.acadtrack.dominioacademico.aluno.SituacaoAcademica;
import g8.acadtrack.dominiocompartilhado.evento.DomainEvent;
import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record RiscoAcademicoEvent(
        UUID eventId,
        Instant occurredAt,
        Long alunoId,
        double mediaGeral,
        NivelRiscoAcademico nivelRisco,
        SituacaoAcademica situacaoAcademica
) implements DomainEvent {

    public RiscoAcademicoEvent {
        Objects.requireNonNull(eventId, "eventId é obrigatório");
        Objects.requireNonNull(occurredAt, "occurredAt é obrigatório");
        Objects.requireNonNull(alunoId, "alunoId é obrigatório");
        Objects.requireNonNull(nivelRisco, "nível de risco é obrigatório");
        Objects.requireNonNull(situacaoAcademica, "situação acadêmica é obrigatória");
    }

    public static RiscoAcademicoEvent criar(
            Long alunoId,
            double mediaGeral,
            NivelRiscoAcademico nivelRisco,
            SituacaoAcademica situacaoAcademica
    ) {
        return new RiscoAcademicoEvent(
                UUID.randomUUID(),
                Instant.now(),
                alunoId,
                mediaGeral,
                nivelRisco,
                situacaoAcademica
        );
    }
}
