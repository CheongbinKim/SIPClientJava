package ai.quantum.qphone.sipclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Slf4j
@MessageEndpoint
public class QSIPClientHandler implements MessageHandler {
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        log.info("Register handleMessage");
        log.info(""+message.getPayload());
        System.out.println("Received UDP message: " + message.getPayload());
    }
}