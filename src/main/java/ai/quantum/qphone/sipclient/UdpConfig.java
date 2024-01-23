package ai.quantum.qphone.sipclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.udp.UnicastReceivingChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Payload;

@Configuration
public class UdpConfig {
    @Value("${sip.port}")
    private int clientPort;

    @Bean
    public UnicastReceivingChannelAdapter udpAdapter() {
        UnicastReceivingChannelAdapter adapter = new UnicastReceivingChannelAdapter(clientPort);
        adapter.setOutputChannelName("udpChannel");
        return adapter;
    }

    @Bean
    public MessageChannel udpChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "udpChannel")
    public UdpMessageHandler udpMessageHandler() {
        return new UdpMessageHandler();
    }
}
