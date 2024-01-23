package ai.quantum.qphone.sipclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.converter.MessageConverter;

@Slf4j
public class UdpMessageHandler implements MessageHandler {
    private final MessageConverter messageConverter;

    public UdpMessageHandler(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        //Object payload = message.getPayload();
        String payloadAsString = (String) messageConverter.fromMessage(message, String.class);

        log.info(payloadAsString);
        //System.out.println("Received UDP message: " + message.getPayload().toString());
    }
}
