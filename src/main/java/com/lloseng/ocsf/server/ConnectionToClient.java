//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lloseng.ocsf.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class ConnectionToClient extends Thread {
    private AbstractServer server;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private boolean readyToStop;
    private HashMap savedInfo = new HashMap(10);

    protected ConnectionToClient(ThreadGroup group, Socket clientSocket, AbstractServer server) throws IOException {
        super(group, (Runnable)null);
        this.clientSocket = clientSocket;
        this.server = server;
        clientSocket.setSoTimeout(0);

        try {
            this.input = new ObjectInputStream(clientSocket.getInputStream());
            this.output = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException var7) {
            try {
                this.closeAll();
            } catch (Exception var6) {
            }

            throw var7;
        }

        this.readyToStop = false;
        this.start();
    }

    public void sendToClient(Object msg) throws IOException {
        if (this.clientSocket != null && this.output != null) {
            this.output.writeObject(msg);
        } else {
            throw new SocketException("socket does not exist");
        }
    }

    public final void close() throws IOException {
        this.readyToStop = true;
        this.closeAll();
    }

    public final InetAddress getInetAddress() {
        return this.clientSocket == null ? null : this.clientSocket.getInetAddress();
    }

    public String toString() {
        return this.clientSocket == null ? null : this.clientSocket.getInetAddress().getHostName() + " (" + this.clientSocket.getInetAddress().getHostAddress() + ")";
    }

    public void setInfo(String infoType, Object info) {
        this.savedInfo.put(infoType, info);
    }

    public Object getInfo(String infoType) {
        return this.savedInfo.get(infoType);
    }

    public final void run() {
        this.server.clientConnected(this);

        try {
            while(!this.readyToStop) {
                try {
                    Object msg = this.input.readObject();
                    if (!this.readyToStop && this.handleMessageFromClient(msg)) {
                        this.server.receiveMessageFromClient(msg, this);
                    }
                } catch (ClassNotFoundException var10) {
                    this.server.clientException(this, var10);
                } catch (RuntimeException var11) {
                    this.server.clientException(this, var11);
                }
            }
        } catch (Exception var12) {
            if (!this.readyToStop) {
                try {
                    this.closeAll();
                } catch (Exception var9) {
                }

                this.server.clientException(this, var12);
            }
        } finally {
            this.server.clientDisconnected(this);
        }

    }

    protected boolean handleMessageFromClient(Object message) {
        return true;
    }

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

    protected void finalize() {
        try {
            this.closeAll();
        } catch (IOException var2) {
        }

    }
}
