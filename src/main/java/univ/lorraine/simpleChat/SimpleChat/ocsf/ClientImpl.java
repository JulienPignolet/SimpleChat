package univ.lorraine.simpleChat.SimpleChat.ocsf;

import com.lloseng.ocsf.client.AbstractClient;

import java.util.ArrayList;
import java.util.List;

class ClientImpl extends AbstractClient {

    private Long id;
    private List<String> messagesEnAttente;
    ClientImpl(Long id, String h, int p) {
        super(h,p);
        this.id = id;
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
        Message message = new Message((String) msg);
        if(!message.getUser_id().equals(this.id)) {
            messagesEnAttente.add((String) msg);
        }
        System.out.println(messagesEnAttenteJSON());
    }

    public String messagesEnAttenteJSON()
    {
        StringBuilder res = new StringBuilder("{");
        for(String msg: messagesEnAttente)
        {
            res.append(msg).append(',');
        }
        if(messagesEnAttente.size() > 0)
            res.deleteCharAt(res.length()-1);
        return res.append('}').toString();
    }
}
