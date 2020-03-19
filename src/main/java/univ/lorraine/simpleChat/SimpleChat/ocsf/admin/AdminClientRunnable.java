package univ.lorraine.simpleChat.SimpleChat.ocsf.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import univ.lorraine.simpleChat.SimpleChat.ocsf.MessageOCSF;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.IOException;

public class AdminClientRunnable implements Runnable {

    Logger logger = LoggerFactory.getLogger(AdminClientImpl.class);

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
//        this.client = new AdminClientImpl( "localhost", 12345);
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
                    logger.warn(e.getMessage());
                    this.stop();
                }
            }
        }
        catch (InterruptedException | IOException e)
        {
            logger.info(e.getMessage());
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
            logger.warn(e.getMessage());
        }
        notify();
    }

    public String getMessagesEnAttente() {
		return this.client.getBuffer();
    }
    
    public void viderBuffer() {
        this.client.viderBuffer();
    }
}

