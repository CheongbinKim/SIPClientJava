package ai.quantum.qphone.sipclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sip")
public class QSIPClientController {
    @Autowired
    QSIPClient client;

    @RequestMapping("/register")
    public String send(){
        client.register();
        return "sent";
    }
}
