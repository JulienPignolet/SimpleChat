package univ.lorraine.simpleChat.SimpleChat.ocsf.groupe;

import univ.lorraine.simpleChat.SimpleChat.ocsf.AutorisationException;
import univ.lorraine.simpleChat.SimpleChat.ocsf.MessageOCSF;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.IOException;

public class GroupeClientRunnable implements Runnable {
    private final GroupeClientImpl client;
    private String msgToSend;
    private Thread thread;
    
    public GroupeClientRunnable(Long id, GroupeClientImpl client) {
        this.client = client;
        msgToSend = null;
        this.thread = null;
    }

    public GroupeClientRunnable(Long id) {
        /******************** REMPLACER SIMPLECHAT.FUN PAR LOCALHOST POUR UTILISER LE SERVEUR OCSF LOCAL ********************/
        this.client = new GroupeClientImpl(id, "localhost", 12345);
        // this.client = new GroupeClientImpl(id, "localhost", 12345);
    }

    @Override
    public synchronized void run() {

        try {
            client.isConnected();
            client.openConnection();
            while (true) {
                try {
                    synchronized (this.client) {
                        client.isConnected();
                        if (msgToSend == null) {
                            wait();
                        }
                        client.sendToServer(msgToSend);
                        msgToSend = null;
                        Thread.sleep(1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    this.stop();
                }
            }
        }
        catch (InterruptedException | IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void start() {
        if (this.thread == null) {
            this.thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if (thread != null) {
            this.thread.interrupt();
            this.thread = null;
        }
    }

    public synchronized void sendMsg(MessageOCSF messageOCSF){
        try (Jsonb jsonb = JsonbBuilder.create()) {
            this.msgToSend = jsonb.toJson(messageOCSF);
        } catch (Exception e) {
            e.printStackTrace();
        }
        notify();
    }

    public void addUserToGroup(long userId) {
        client.addUserToGroup(userId);
    }

    public synchronized String getMessagesEnAttente(long userId) throws AutorisationException, InterruptedException {
        while(this.client.getBufferById(userId).isEmpty())
            wait();
        return this.client.getBufferById(userId);
    }

    public String getMessagesObjetsEnAttente(long userId) throws AutorisationException {
        return this.client.getBufferById(userId);
    }

    
    public void viderBuffer(long userId) throws AutorisationException {
        this.client.viderBuffer(userId);
    }
}

