package univ.lorraine.simpleChat.SimpleChat.ocsf;

import java.io.IOException;

public class ClientRunnable implements Runnable {
    private ClientImpl client;
    private String msgToSend;
    private Thread thread;

    public ClientRunnable(ClientImpl client) {
        this.client = client;
        msgToSend = null;
        this.thread = null;

    }

    public ClientRunnable() {
        this.client = new ClientImpl("localhost", 12345);
    }

    @Override
    public void run() {
        try {
            while (true) {
                try {
                    client.isConnected();
                    client.openConnection();
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
        catch (InterruptedException e)
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
}

