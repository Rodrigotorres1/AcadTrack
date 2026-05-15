package g8.acadtrack.infraestrutura.evento;

import g8.acadtrack.aplicacao.evento.DomainEventPublisher;
import g8.acadtrack.dominiocompartilhado.evento.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher publisher;

    public SpringDomainEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publicar(DomainEvent evento) {
        publisher.publishEvent(evento);
    }
}
