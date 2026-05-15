package g8.acadtrack.aplicacao.evento;

import g8.acadtrack.dominiocompartilhado.evento.DomainEvent;

import java.util.Collection;

public interface DomainEventPublisher {

    void publicar(DomainEvent evento);

    default void publicar(Collection<? extends DomainEvent> eventos) {
        eventos.forEach(this::publicar);
    }
}
