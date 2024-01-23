package ai.quantum.qphone.sipclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class UdpMessageHandler implements MessageHandler {
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        Object payload = message.getPayload();

        if (payload instanceof byte[]) {
            byte[] payloadBytes = (byte[]) payload;

            // Convert byte array to String using a specific character encoding
            String payloadAsString = new String(payloadBytes, StandardCharsets.UTF_8);

            // Now you can use payloadAsString as needed
            System.out.println("Received message as String: " + payloadAsString);
        } else {
            System.out.println("Received message with non-byte payload");
        }

        //log.info(message.getPayload().toString());
        //System.out.println("Received UDP message: " + message.getPayload().toString());
    }
}
