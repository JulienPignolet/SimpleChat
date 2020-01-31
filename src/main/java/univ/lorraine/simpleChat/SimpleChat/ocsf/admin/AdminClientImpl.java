package univ.lorraine.simpleChat.SimpleChat.ocsf.admin;

import com.lloseng.ocsf.client.ObservableClient;
import univ.lorraine.simpleChat.SimpleChat.ocsf.MessageOCSF;
import univ.lorraine.simpleChat.SimpleChat.ocsf.UserBuffer;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

/**
 * Représente un groupe de discussion
 */
class AdminClientImpl extends ObservableClient {

    private UserBuffer userBuffer;
    
    AdminClientImpl(String host, int port) {
        super(host, port);
        userBuffer = new UserBuffer(0);
    }

    @Override
	protected void connectionClosed() {
        System.out.println("Admin: Closed");
        System.out.println("Admin.isConnected()="+isConnected());
    }

    @Override
    protected void connectionException(Exception exception) {
        exception.printStackTrace();
    }

    @Override
    protected void connectionEstablished() {
        System.out.println("Admin : Connected");
//        System.out.println("Admin.isConnected()="+isConnected());
    }

    @Override
    protected void handleMessageFromServer(Object msg){
        System.out.println("Admin : Message received = " + msg);
        try (Jsonb jsonb = JsonbBuilder.create()) {
            MessageOCSF messageOCSF = jsonb.fromJson((String) msg, MessageOCSF.class);
            userBuffer.addMessageToBuffer(messageOCSF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getBuffer()  {
        return userBuffer.getMsgBuffer();
    }
    
    public void viderBuffer() { // /!\ NE SERT ACTUELLEMENT PAS
            userBuffer.clearBufferFromMessages();
    }
}
