package com.streaming.main;

import com.streaming.main.model.Operation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.servlet.http.HttpSession;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/api")
public class ConnectController {

    @Autowired
    DataRepository messageRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/connect")
    public ResponseEntity<?> connectToServer(
            @RequestParam(value = "link") String link,
            @RequestParam(value = "operation") Operation operation,
            HttpSession session
    ) {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        System.out.println(session.getId());
        StompSessionHandler sessionHandler = new MyStompSessionHandler(session.getId(), operation, messageRepository, rabbitTemplate);
        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
        stompClient.connect(link, webSocketHttpHeaders, sessionHandler);

        return new ResponseEntity<>(session.getId(), HttpStatus.OK);
    }

    @GetMapping("/close")
    public ResponseEntity<?> closeConnectToServer(HttpSession session) {
        Connections.getSingleton().closeConnection(session.getId());
        return new ResponseEntity<>(session.getId(), HttpStatus.OK);
    }
}
