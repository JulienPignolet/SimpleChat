package univ.lorraine.simpleChat.SimpleChat.ocsf.groupe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import univ.lorraine.simpleChat.SimpleChat.ocsf.AutorisationException;

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
        this.client = new GroupeClientImpl(id, "simplechat.fun", 12345);
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

    public synchronized void sendMsg(String message, String name) {
        JsonObject json = new JsonParser().parse(message).getAsJsonObject();
        json.addProperty("user_name", name);
        this.msgToSend = json.toString();
        notify();
    }

    public void addUserToGroup(long user_id) {
        client.addUserToGroup(user_id);
    }

    public String getMessagesEnAttente(long user_id) throws AutorisationException {
		    return this.client.getBufferById(user_id);
    }

    public String getMessagesObjetsEnAttente(long user_id) throws AutorisationException {
        return this.client.getBufferById(user_id);
    }

    
    public void viderBuffer(long user_id) throws AutorisationException {
        this.client.viderBuffer(user_id);
    }
}

