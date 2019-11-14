package univ.lorraine.simpleChat.SimpleChat.ocsf;

import java.io.IOException;
import java.util.List;

public class ClientRunnable implements Runnable {
    private ClientImpl client;
    private String msgToSend;
    private Thread thread;
    private Long id;

    public ClientRunnable(Long id, ClientImpl client) {
        this.id = id;
        this.client = client;
        msgToSend = null;
        this.thread = null;

    }

    public ClientRunnable(Long id) {
        this.client = new ClientImpl(id, "localhost", 12345);
        this.id = id;
    }

    @Override
    public void run() {
        try {
            client.isConnected();
            client.openConnection();
            while (true) {
                try {
                    client.isConnected();
                    if (msgToSend != null) {
                        client.sendToServer(msgToSend);
                        msgToSend = null;
                        Thread.sleep(1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (InterruptedException | IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void start()
    {
        if (this.thread == null)
        {
            this.thread = new Thread(this);
            thread.start();
        }
    }

    public void stop()
    {
        if (thread != null)
        {
            this.thread.interrupt();
            this.thread = null;
        }
    }

    public void sendMsg(String msg)
    {
        this.msgToSend=msg;
    }

    public String getMessagesEnAttenteJSON()
    {
        return this.client.getMessagesEnAttenteJSON();
    }
    public List<Message> getMessagesEnAttente()
    {
        return this.client.getMessagesEnAttente();
    }
}

