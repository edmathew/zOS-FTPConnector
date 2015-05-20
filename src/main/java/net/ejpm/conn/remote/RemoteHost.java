package net.ejpm.conn.remote;

public interface RemoteHost {

    public void connect() throws RemoteHostException;

    public boolean login() throws RemoteHostException;

    public void disconnect() throws RemoteHostException;

}
