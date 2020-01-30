//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lloseng.ocsf.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public abstract class AbstractClient implements Runnable {
    private Socket clientSocket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Thread clientReader;
    private boolean readyToStop = false;
    private String host;
    private int port;

    public AbstractClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public final void openConnection() throws IOException {
        if (!this.isConnected()) {
            try {
                this.clientSocket = new Socket(this.host, this.port);
                this.output = new ObjectOutputStream(this.clientSocket.getOutputStream());
                this.input = new ObjectInputStream(this.clientSocket.getInputStream());
            } catch (IOException var4) {
                try {
                    this.closeAll();
                } catch (Exception var3) {
                }

                throw var4;
            }

            this.clientReader = new Thread(this);
            this.readyToStop = false;
            this.clientReader.start();
        }
    }

    public void sendToServer(Object msg) throws IOException {
        if (this.clientSocket != null && this.output != null) {
            this.output.writeObject(msg);
        } else {
            throw new SocketException("socket does not exist");
        }
    }

    public final void closeConnection() throws IOException {
        this.readyToStop = true;
        this.closeAll();
    }

    public final boolean isConnected() {
        return this.clientReader != null && this.clientReader.isAlive();
    }

    public final int getPort() {
        return this.port;
    }

    public final void setPort(int port) {
        this.port = port;
    }

    public final String getHost() {
        return this.host;
    }

    public final void setHost(String host) {
        this.host = host;
    }

    public final InetAddress getInetAddress() {
        return this.clientSocket.getInetAddress();
    }

    public final void run() {
        this.connectionEstablished();

        try {
            while(!this.readyToStop) {
                try {
                    Object msg = this.input.readObject();
                    if (!this.readyToStop) {
                        this.handleMessageFromServer(msg);
                    }
                } catch (ClassNotFoundException var11) {
                    this.connectionException(var11);
                } catch (RuntimeException var12) {
                    this.connectionException(var12);
                }
            }
        } catch (Exception var13) {
            if (!this.readyToStop) {
                try {
                    this.closeAll();
                } catch (Exception var10) {
                }

                this.clientReader = null;
                this.connectionException(var13);
            }
        } finally {
            this.clientReader = null;
            this.connectionClosed();
        }

    }

    protected void connectionClosed() {
    }

    protected void connectionException(Exception exception) {
    }

    protected void connectionEstablished() {
    }

    protected abstract void handleMessageFromServer(Object var1);

    private final void closeAll() throws IOException {
        try {
            if (this.clientSocket != null) {
                this.clientSocket.close();
            }

            if (this.output != null) {
                this.output.close();
            }

            if (this.input != null) {
                this.input.close();
            }
        } finally {
            this.output = null;
            this.input = null;
            this.clientSocket = null;
        }

    }
}
