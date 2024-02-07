package ai.quantum.qphone.sipclient;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "udpOutboundChannel")
public interface UdpGateway {
    void send(@Header("ip") String ip, @Header("port") int port, String message);
}