package ai.quantum.qphone.sipclient;

import gov.nist.javax.sip.header.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.*;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class QSIPClient {
    private int cSeq = 1;

    @Value("${ari.host}")
    private String asteriskHost;

    @Value("${ari.port}")
    private int asteriskPort;

    @Value("${sip.host}")
    private String sipHostname;

    @Value("${sip.username}")
    private String sipUserName;

    @Value("${sip.password}")
    private String sipPassword;

    @Value("${sip.port}")
    private int sipPort;

    public QSIPClient(){

    }

    public void register(){
        /*** EXAMPLE
         REGISTER sip:192.168.0.165:5061 SIP/2.0
         Via: SIP/2.0/UDP 172.18.0.3:13000;rport;branch=9ltis0iohr3bm4in94zfxoqmeatcg3d73hm
         From: <sip:9999@172.18.0.3:13000>;tag=xk7588628423
         To: <sip:9999@192.168.0.165:5061>
         Max-Forwards: 70
         Call-ID: otzpm76o6fa64d275tbyve44v9fx86vjmuy
         CSeq: 25555 REGISTER
         Contact: <sip:9999@172.18.0.3:13000>
         Allow: ACK,BYE,CANCEL,INVITE,REGISTER,UPDATE,MESSAGE,INFO,OPTIONS,SUBSCRIBE,NOTIFY,REFER,COMET,PUBLISH,PING,DO,SHARED

         Allow-Events: presence,refer,telephone-event,keep-alive,dialog
         Supported: replaces, timer
         Event: registration
         User-Agent: QuantumPhone 0.1
         Expires: 600
         Accept: application/sdp,application/dtmf-relay,audio/telephone-event,message/sipfrag,text/plain,text/html
         Content-Length: 0
         */
        try {
            // SIP Client 호스트 주소, nat 환경 구성에 따라 변경해야 할 수 있음
            //sipHostname = InetAddress.getLocalHost().getHostAddress();
            //sipHostname = "192.168.0.164";
            // SIP Factory 생성
            SipFactory sipFactory = SipFactory.getInstance();
            sipFactory.setPathName("gov.nist");

            MessageFactory messageFactory = sipFactory.createMessageFactory();
            AddressFactory addressFactory = sipFactory.createAddressFactory();
            HeaderFactory headerFactory = sipFactory.createHeaderFactory();

            // SIP URI 생성
            SipURI fromUri = addressFactory.createSipURI(sipUserName, sipHostname);
            Address fromAddress = addressFactory.createAddress(fromUri);

            SipURI toUri = addressFactory.createSipURI("asterisk", asteriskHost);
            toUri.setPort(asteriskPort);
            Address toAddress = addressFactory.createAddress(toUri);

            FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, "26cc3otqqj");
            ToHeader toHeader = headerFactory.createToHeader(toAddress, null);

            CallIdHeader callIdHeader = headerFactory.createCallIdHeader("myCallID");
            CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(1L, Request.REGISTER);
            MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);

            // Via 헤더 생성
            ArrayList<ViaHeader> viaHeaders = new ArrayList<>();
            ViaHeader viaHeader = headerFactory.createViaHeader(sipHostname, sipPort, "udp", null);
            viaHeaders.add(viaHeader);

            // Contact 헤더 생성
            SipURI contactUri = addressFactory.createSipURI(sipUserName, sipHostname);
            contactUri.setPort(sipPort);
            Address contactAddress = addressFactory.createAddress(contactUri);
            ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);



            // Allow 헤더 생성
            String methods = Request.INVITE + ", " + Request.ACK + ", "
                    + Request.OPTIONS + ", " + Request.CANCEL + ", " + Request.BYE
                    + ", " + Request.INFO + ", " + Request.REFER + ", "
                    + Request.MESSAGE + ", " + Request.NOTIFY + ", "
                    + Request.SUBSCRIBE;
            AllowHeader allowHeader = headerFactory.createAllowHeader(methods);

            // Request 생성
            Request request = messageFactory.createRequest(
                    toUri,
                    Request.REGISTER,
                    callIdHeader,
                    cSeqHeader,
                    fromHeader,
                    toHeader,
                    viaHeaders,
                    maxForwards
            );

            /**
             * Allow-Events: presence,refer,telephone-event,keep-alive,dialog
             *          Supported: replaces, timer
             *          Event: registration
             *          User-Agent: QuantumPhone 0.1
             *          Expires: 600
             *          Accept: application/sdp,application/dtmf-relay,audio/telephone-event,message/sipfrag,text/plain,text/html
             *          Content-Length: 0
             */

            AllowEventsHeader allowEventsHeader = headerFactory.createAllowEventsHeader("presence,refer,telephone-event,keep-alive,dialog");
            SupportedHeader supportedHeader = headerFactory.createSupportedHeader("replaces, timer");
            EventHeader eventHeader = headerFactory.createEventHeader("registration");
            List<String> agents = new ArrayList<>();
            agents.add("QuantumPhone 0.1");
            UserAgentHeader userAgentHeader = headerFactory.createUserAgentHeader(agents);
            ExpiresHeader expiresHeader = headerFactory.createExpiresHeader(3600);
            ContentLengthHeader contentLengthHeader = headerFactory.createContentLengthHeader(0);


            request.addHeader(contactHeader);
            request.addHeader(allowHeader);
            request.addHeader(allowEventsHeader);
            request.addHeader(supportedHeader);
            request.addHeader(eventHeader);
            request.addHeader(userAgentHeader);
            request.addHeader(contentLengthHeader);

            request.addHeader(expiresHeader);

            // 생성된 REGISTER Request 출력
            System.out.println("Created REGISTER Request:\n" + request);

            UnicastSendingMessageHandler handler = new UnicastSendingMessageHandler(asteriskHost, asteriskPort);
            String payload = request.toString();
            handler.handleMessage(MessageBuilder.withPayload(payload).build());
            handler.stop();
        } catch (ParseException | InvalidArgumentException | SipException  e) {
            throw new RuntimeException(e);
        }
    }
}
