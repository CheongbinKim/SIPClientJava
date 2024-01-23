package ai.quantum.qphone.sipclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.messaging.MessageChannel;

@Configuration
public class UdpSendConfig {
    @Value("${sip.port}")
    private int clientPort;

    @Bean
    @ServiceActivator(inputChannel = "udpOutboundChannel")
    public UnicastSendingMessageHandler udpSendingMessageHandler() {
        UnicastSendingMessageHandler handler = new UnicastSendingMessageHandler("localhost", clientPort);
        //handler.setOutputChannelName("udpOutboundChannel");
        return handler;
    }

    @Bean
    public MessageChannel udpOutboundChannel() {
        return new DirectChannel();
    }
}
