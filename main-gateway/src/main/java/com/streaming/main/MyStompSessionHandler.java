package com.streaming.main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.streaming.main.model.Data;
import com.streaming.main.model.Message;
import com.streaming.main.model.Operation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    String userId;
    Operation operation;
    Long numberMessage = 0l;

    DataRepository dataRepository;
    RabbitTemplate rabbitTemplate;


    public MyStompSessionHandler(String userId, Operation operation, DataRepository dataRepository, RabbitTemplate rabbitTemplate) {
        this.userId = userId;
        this.operation = operation;
        this.dataRepository = dataRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/topic/greetings", this);
        Connections.getSingleton().saveSocketConnection(session, userId);
        System.out.println("Subscribed to /topic/greetings");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return null;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        try {
            String cadena = new String((byte[]) payload);
            System.out.println(cadena);

            Data data = dataRepository.findOne(userId);

            Message message = objectMapper.readValue(cadena, Message.class);


            if (data == null) {
                List<Message> messages = new ArrayList<>();
                messages.add(message);

                String s = objectMapper.writeValueAsString(messages);
                final Data build = Data.builder()
                        .id(userId)
                        .messageListJSON(s)
                        .numberMessage(1l)
                        .operation(operation)
                        .isFinal(false)
                        .build();

                dataRepository.save(build);
            } else {
                List<Message> messages = objectMapper.readValue(data.getMessageListJSON(), new TypeReference<List<Message>>() {
                });

                messages.add(message);

                if (messages.size() <= 10) {
                    String s = objectMapper.writeValueAsString(messages);

                    data.setMessageListJSON(s);

                    dataRepository.delete(data.getId());
                    dataRepository.save(data);
                } else {
                    final String s = objectMapper.writeValueAsString(data);
                    rabbitTemplate.convertAndSend("spring-boot", s);

                    List<Message> messageList = new ArrayList<>();
                    String messageListS = objectMapper.writeValueAsString(messageList);
                    data.setMessageListJSON(messageListS);
                    data.setNumberMessage(data.getNumberMessage() + 1l);
                    dataRepository.delete(data.getId());
                    dataRepository.save(data);
                }

            }


            System.out.println("-->");

        } catch (IOException e) {

        }
    }
}
