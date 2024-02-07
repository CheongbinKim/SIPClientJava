package ai.quantum.qphone.sipclient;

import java.util.Map;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.udp.UnicastReceivingChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class QSIPClientConfig {
    @Value("${sip.port}")
    private int clientPort;

    @Autowired
    QSIPClient client;

    @PostConstruct
    public void init(){

    }

    @Bean
    public MessageChannel udpInputChannel(){
        return new DirectChannel();
    }
    @Bean
    public MessageChannel udpOutputChannel() {
        return new DirectChannel();
    }



    @Bean
    public UnicastReceivingChannelAdapter udpIn() {
        UnicastReceivingChannelAdapter adapter = new UnicastReceivingChannelAdapter(clientPort);
        adapter.setOutputChannelName("udpInputChannel");
        adapter.setTaskExecutor(executor());
        return adapter;
    }

    @Bean
    public TaskExecutor executor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(5);
        return exec;
    }


    @Transformer(inputChannel = "udpInputChannel", outputChannel = "udpOutputChannel")
    public String transformer(@Payload byte[] payload, @Headers Map<String, Object> headers) {
        String message = new String(payload);
        return message;
    }

    @Bean
    @ServiceActivator(inputChannel = "udpOutputChannel")
    public MessageHandler udpMsgHandler() {
        return new QSIPClientHandler();
    }
}