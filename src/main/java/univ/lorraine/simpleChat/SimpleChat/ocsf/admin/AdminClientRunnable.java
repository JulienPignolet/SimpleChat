package univ.lorraine.simpleChat.SimpleChat.ocsf.admin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

public class AdminClientRunnable implements Runnable {
    private final AdminClientImpl client;
    private String msgToSend;
    private Thread thread;

    public AdminClientRunnable(AdminClientImpl client) {
        this.client = client;
        msgToSend = null;
        this.thread = null;
    }

    public AdminClientRunnable() {
        /******************** REMPLACER SIMPLECHAT.FUN PAR LOCALHOST POUR UTILISER LE SERVEUR OCSF LOCAL ********************/
        this.client = new AdminClientImpl( "simplechat.fun", 12345);
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

    public String getMessagesEnAttente() {
		return this.client.getBuffer();
    }
    
    public void viderBuffer() {
        this.client.viderBuffer();
    }
}

