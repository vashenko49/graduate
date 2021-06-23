package com.simulate.streaming;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ScheduledUpdatesOnTopic {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private CSVService csvService;

    @Scheduled(fixedDelay = 600)
    public void publishUpdates() {
        AtomicInteger index = csvService.getIndex();

        if (index.get() > csvService.getMessages().size()) {
            index.set(0);
        }
        template.convertAndSend("/topic/greetings", csvService.getMessages().get(index.get()));
        System.out.println(csvService.getMessages().get(index.get()));

        index.incrementAndGet();

    }
}