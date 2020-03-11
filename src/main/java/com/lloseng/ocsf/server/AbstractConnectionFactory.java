//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lloseng.ocsf.server;

import java.io.IOException;
import java.net.Socket;

public abstract class AbstractConnectionFactory {
    public AbstractConnectionFactory() {
    }

    protected abstract ConnectionToClient createConnection(ThreadGroup var1, Socket var2, AbstractServer var3) throws IOException;
}
