package com.test.ecommerceorderservice.infrastructure.events;

import com.test.ecommerceorderservice.application.event.DomainEvent;
import com.test.ecommerceorderservice.shared.events.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Component
public class InMemoryEventPublisher implements DomainEventPublisher {

    private final BlockingQueue<DomainEvent> queue = new LinkedBlockingQueue<>();

    @Override
    public void publish(DomainEvent event) {
        queue.offer(event);
        processEventAsync();
    }

    @Async
    public void processEventAsync() {
        DomainEvent event;
        while ((event = queue.poll()) != null) {
            try {
                // simulate sending notification
                log.info("Notify admins asynchronously about order {} event {}",
                        event.getAggregateId(), event.getType());
            } catch (Exception e) {
                log.error("Failed to deliver event {}, retrying", event.getAggregateId());
                queue.offer(event);
            }
        }
    }
}