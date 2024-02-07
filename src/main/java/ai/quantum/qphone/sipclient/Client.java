package ai.quantum.qphone.sipclient;

import gov.nist.javax.sip.ListeningPointImpl;
import gov.nist.javax.sip.stack.NioMessageProcessorFactory;

import javax.sip.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.TooManyListenersException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.URI;
import javax.sip.header.*;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;

public class Client implements SipListener{

    private int CLIENT_PORT = 13000;

    private int SERVER_PORT = 5061;

    protected String testProtocol = "udp";

    public String host = "192.168.0.165";

    public HeaderFactory headerFactory;

    public MessageFactory messageFactory;

    public AddressFactory addressFactory;

    private SipFactory sipFactory;
    private SipStack sipStack;
    private SipProvider provider;
    private boolean o_sentInvite, o_received180, o_sentCancel, o_receiver200Cancel,
            o_inviteTxTerm, o_dialogTerinated;

    public Client() {
        try {
            final Properties defaultProperties = new Properties();
            String host = "localhost";
            defaultProperties.setProperty("javax.sip.STACK_NAME", "client");
            defaultProperties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "DEBUG");
            defaultProperties.setProperty("gov.nist.javax.sip.DEBUG_LOG", "client_debug.txt");
            defaultProperties.setProperty("gov.nist.javax.sip.SERVER_LOG", "client_log.txt");
            defaultProperties.setProperty("gov.nist.javax.sip.READ_TIMEOUT", "1000");
            defaultProperties.setProperty("gov.nist.javax.sip.CACHE_SERVER_CONNECTIONS","false");
            if(System.getProperty("enableNIO") != null && System.getProperty("enableNIO").equalsIgnoreCase("true")) {
                defaultProperties.setProperty("gov.nist.javax.sip.MESSAGE_PROCESSOR_FACTORY", NioMessageProcessorFactory.class.getName());
            }
            this.sipFactory = SipFactory.getInstance();
            this.sipFactory.setPathName("gov.nist");
            this.sipStack = this.sipFactory.createSipStack(defaultProperties);
            this.sipStack.start();
//            ListeningPoint lp = this.sipStack.createListeningPoint(host, CLIENT_PORT, testProtocol);
//            this.provider = this.sipStack.createSipProvider(lp);
//            headerFactory = this.sipFactory.createHeaderFactory();
//            messageFactory = this.sipFactory.createMessageFactory();
//            addressFactory = this.sipFactory.createAddressFactory();
//            this.provider.addSipListener(this);
        } catch (PeerUnavailableException e) {
            throw new RuntimeException("PeerUnavailableException");
            //Assert.fail("unexpected exception ");
        } catch (TransportNotSupportedException e) {
            throw new RuntimeException(e);
        } catch (ObjectInUseException e) {
            throw new RuntimeException(e);
        } catch (SipException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMultipleContactRegistration() throws Exception {
        Address fromAddress = addressFactory.createAddress("here@somewhere:5070");
        ContactHeader contactHeader1 = headerFactory.createContactHeader(addressFactory.createAddress("sip:9999@somewhere:5070"));
        //ContactHeader contactHeader2 = headerFactory.createContactHeader(addressFactory.createAddress("sip:here@somewhereelse:5080"));


        CallIdHeader callId = provider.getNewCallId();
        CSeqHeader cSeq = headerFactory.createCSeqHeader(1l, Request.REGISTER);
        FromHeader from = headerFactory.createFromHeader(fromAddress, "1234");
        ToHeader to = headerFactory.createToHeader(addressFactory.createAddress("server@"+host+":"+SERVER_PORT), null);
        List via = Arrays.asList(((ListeningPointImpl)provider.getListeningPoint(testProtocol)).getViaHeader());
        MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(10);

        URI requestURI = addressFactory.createURI("sip:test@"+host+":"+SERVER_PORT);
        Request request = messageFactory.createRequest(requestURI, Request.REGISTER, callId, cSeq, from, to, via, maxForwards);

        request.setRequestURI(requestURI);
        request.addHeader(contactHeader1);
        //request.addHeader(contactHeader2);
        ClientTransaction ctx = this.provider.getNewClientTransaction(request);
        ctx.sendRequest();
    }

    @Override
    public void processRequest(RequestEvent requestEvent) {

    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {

    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {

    }

    @Override
    public void processIOException(IOExceptionEvent ioExceptionEvent) {

    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {

    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {

    }
}