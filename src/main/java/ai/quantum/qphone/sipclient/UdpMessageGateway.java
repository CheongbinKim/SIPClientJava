package ai.quantum.qphone.sipclient;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;
@MessagingGateway(defaultRequestChannel = "udpOutboundChannel")
public interface UdpMessageGateway {
    void send(@Payload String message);
}
