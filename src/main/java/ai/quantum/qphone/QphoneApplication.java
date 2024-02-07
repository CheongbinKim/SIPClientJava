package ai.quantum.qphone;

import ai.quantum.qphone.sipclient.Client;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QphoneApplication {


	public static void main(String[] args) {
		SpringApplication.run(QphoneApplication.class, args);
		//Client client = new Client();
	}
}