package com.network.com;

import javax.imageio.IIOException;
import java.io.*;
import java.net.Socket;

public class TCPConnection {

    private final Socket socket;
    private final Thread rxTread;
    private final BufferedReader in;
    private final TCPConnectionListner eventListner;
    private final BufferedWriter out;

    public TCPConnection(TCPConnectionListner eventListner, String ipAdress, int port) throws IOException {
        this(eventListner, new Socket(ipAdress, port));

    }

    public TCPConnection(TCPConnectionListner eventListner, Socket socket) throws IOException {
        this.eventListner = eventListner;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
        rxTread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListner.onConnectionReady(TCPConnection.this);
                    while (!rxTread.isInterrupted()) {
                        eventListner.onReceiveString(TCPConnection.this, in.readLine());
                    }
                } catch (IOException e) {
                    eventListner.onExeption(TCPConnection.this, e);
                } finally {
                    eventListner.onDisconnect(TCPConnection.this);
                }
            }
        });

        rxTread.start();

    }

    public synchronized void sentString(String value) {
        try {
            out.write(value + "\r\n ");
            out.flush();
        } catch (IOException e) {
            eventListner.onExeption(TCPConnection.this, e);
            disconnect();

        }
    }

    public synchronized void disconnect() {
        rxTread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListner.onExeption(TCPConnection.this, e);
        }

    }

    @Override
    public String toString() {
        return "TCPConnection" + socket.getInetAddress() + ": " + socket.getPort();
    }
}
