package univ.lorraine.simpleChat.SimpleChat.ocsf.admin;

import com.lloseng.ocsf.client.ObservableClient;
import univ.lorraine.simpleChat.SimpleChat.ocsf.Message;
import univ.lorraine.simpleChat.SimpleChat.ocsf.UserBuffer;

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
        System.out.println("Client: Closed");
        System.out.println("Client.isConnected()="+isConnected());
    }

    @Override
    protected void connectionException(Exception exception) {
        exception.printStackTrace();
    }

    @Override
    protected void connectionEstablished() {
        System.out.println("Client : Connected");
//        System.out.println("Client.isConnected()="+isConnected());
    }

    protected void handleMessageFromServer(Object msg) {
        System.out.println("Client : Message received = " + msg);
        userBuffer.addMessageToBuffer(new Message((String) msg));
    }

    public String getBuffer()  {
        return userBuffer.getMsgBuffer();
    }
    
    public void viderBuffer() { // /!\ NE SERT ACTUELLEMENT PAS
            userBuffer.clearBufferFromMessages();
    }
}
