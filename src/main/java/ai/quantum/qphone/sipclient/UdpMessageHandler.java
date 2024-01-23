package ai.quantum.qphone.sipclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Slf4j
public class UdpMessageHandler implements MessageHandler {

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        log.info(message.getPayload().toString());
        //System.out.println("Received UDP message: " + message.getPayload().toString());
    }
}
