package univ.lorraine.simpleChat.SimpleChat.ocsf.admin;

import com.lloseng.ocsf.client.ObservableClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import univ.lorraine.simpleChat.SimpleChat.ocsf.MessageOCSF;
import univ.lorraine.simpleChat.SimpleChat.ocsf.UserBuffer;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

/**
 * Représente un groupe de discussion
 */
class AdminClientImpl extends ObservableClient {
    Logger logger = LoggerFactory.getLogger(AdminClientImpl.class);

    private UserBuffer userBuffer;
    
    AdminClientImpl(String host, int port) {
        super(host, port);
        userBuffer = new UserBuffer(0);
    }

    @Override
	protected void connectionClosed() {
        logger.info("Admin: Closed");
        logger.info("Admin.isConnected()="+isConnected());
    }

    @Override
    protected void connectionException(Exception exception) {
        exception.printStackTrace();
    }

    @Override
    protected void connectionEstablished() {
        logger.info("Admin : Connected");
//        logger.info("Admin.isConnected()="+isConnected());
    }

    @Override
    protected void handleMessageFromServer(Object msg){
        logger.info("Admin : Message received = " + msg);
        try (Jsonb jsonb = JsonbBuilder.create()) {
            MessageOCSF messageOCSF = jsonb.fromJson((String) msg, MessageOCSF.class);
            userBuffer.addMessageToBuffer(messageOCSF);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public String getBuffer()  {
        return userBuffer.getMsgBuffer();
    }
    
    public void viderBuffer() { // /!\ NE SERT ACTUELLEMENT PAS
            userBuffer.clearBufferFromMessages();
    }
}
