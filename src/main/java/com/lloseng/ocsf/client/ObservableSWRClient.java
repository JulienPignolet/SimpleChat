//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lloseng.ocsf.client;

import java.util.ArrayList;
import java.util.List;

public class ObservableSWRClient extends ObservableClient {
    public static final String WAITING_FOR_REPLY = "#OC:Waiting for reply.";
    private ArrayList expected = new ArrayList(3);
    private boolean cancelled = false;
    private int waitTime = 30000;
    private Exception exception;
    private Object received;

    public ObservableSWRClient(String host, int port) {
        super(host, port);
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public synchronized boolean connectAndWait() throws Exception {
        this.clearAll();
        this.expected.add("#OC:Connection established.");
        this.openConnection();

        while(!this.cancelled && !this.expected.isEmpty()) {
            this.wait((long)this.waitTime);
            this.setChanged();
            this.notifyObservers("#OC:Waiting for reply.");
        }

        if (this.exception != null) {
            throw this.exception;
        } else {
            return !this.cancelled;
        }
    }

    public synchronized Object sendAndWaitForReply(Object message, Object expectedObject) throws Exception {
        this.clearAll();
        this.expected.add(expectedObject);
        return this.sendAndWaitForReply(message, (List)null);
    }

    public synchronized Object sendAndWaitForReply(Object message, List expectedListOfObject) throws Exception {
        if (expectedListOfObject != null) {
            this.clearAll();
            this.expected.addAll(expectedListOfObject);
        }

        this.sendToServer(message);

        while(!this.cancelled && !this.expected.isEmpty()) {
            this.wait((long)this.waitTime);
            this.setChanged();
            this.notifyObservers("#OC:Waiting for reply.");
        }

        if (this.exception != null) {
            throw this.exception;
        } else {
            return this.cancelled ? null : this.received;
        }
    }

    public synchronized void cancel() {
        this.clearAll();
        this.cancelled = true;
        this.notifyAll();
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    private void clearAll() {
        this.cancelled = false;
        this.expected.clear();
        this.exception = null;
        this.received = null;
    }

    private synchronized void notify(Exception ex) {
        this.clearAll();
        this.exception = ex;
        this.notifyAll();
    }

    private synchronized void receive(Object ob) {
        if (this.expected.contains(ob)) {
            this.clearAll();
            this.received = ob;
            this.notifyAll();
        }

    }

    protected void handleMessageFromServer(Object message) {
        this.receive(message);
        this.setChanged();
        this.notifyObservers(message);
    }

    protected void connectionClosed() {
        this.notify((Exception)null);
        this.setChanged();
        this.notifyObservers("#OC:Connection closed.");
    }

    protected void connectionException(Exception exception) {
        this.notify(exception);
        this.setChanged();
        this.notifyObservers(exception);
    }

    protected void connectionEstablished() {
        this.receive("#OC:Connection established.");
        this.setChanged();
        this.notifyObservers("#OC:Connection established.");
    }
}
