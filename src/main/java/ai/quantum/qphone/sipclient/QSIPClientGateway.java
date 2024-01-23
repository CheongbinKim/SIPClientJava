package ai.quantum.qphone.sipclient;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "udpOutputChannel")
public interface  QSIPClientGateway {
    void send(String message);
}
