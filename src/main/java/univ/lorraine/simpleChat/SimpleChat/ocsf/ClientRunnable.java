package univ.lorraine.simpleChat.SimpleChat.ocsf;

import java.io.IOException;
import java.util.List;

public class ClientRunnable implements Runnable {
    private ClientImpl client;
    private String msgToSend;
    private Thread thread;
    
    public ClientRunnable(Long id, ClientImpl client) {
        this.client = client;
        msgToSend = null;
        this.thread = null;
    }

    public ClientRunnable(Long id) {
        this.client = new ClientImpl(id, "localhost", 12345);
    }

    @Override
    public void run() {

		// ATTENTION : Il faut demander au serveur d'enregistrer le message dans la BDD
    	
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
    
    public void sendMsg(String msg) {
        this.msgToSend = msg;
    }
    
    /**
     * Retourne un objet JSON contenant tous le buffer de l'utilisateur
     * @param user_id
     * @return String
     * @throws AutorisationException
     */
    public String getMessagesEnAttenteJSON(long user_id) throws AutorisationException {
		return this.client.getBufferById(user_id);
    }

    public void viderBuffer(long user_id) throws AutorisationException {
        this.client.viderBuffer(user_id);
    }
}

