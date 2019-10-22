package univ.lorraine.simpleChat.SimpleChat.ocsf;

import com.lloseng.ocsf.client.AbstractClient;

import java.util.ArrayList;
import java.util.List;

class ClientImpl extends AbstractClient {

    private List<String> messagesEnAttente;
    ClientImpl(String h, int p) {
        super(h,p);
        messagesEnAttente = new ArrayList<>();
    }

    protected void connectionClosed() {

        System.out.println("Client: Closed");
        System.out.println("Client.isConnected()="+isConnected());
    }

    protected void connectionException(Exception exception) {

        System.out.println("Client exception: " + exception);
    }

    protected void connectionEstablished() {

        System.out.println("Client: Connected");
        System.out.println("Client.isConnected()="+isConnected());
    }

    protected void handleMessageFromServer(Object msg){

        System.out.println("Client: Message received=" +msg);
        messagesEnAttente.add((String) msg);
        System.out.println(messagesEnAttenteJSON());
    }

    private String messagesEnAttenteJSON()
    {
        StringBuilder res = new StringBuilder("{");
        for(String msg: messagesEnAttente)
        {
            res.append(msg);
        }
        return res+"}";
    }
}
