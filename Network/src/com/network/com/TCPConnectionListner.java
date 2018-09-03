package com.network.com;

public interface TCPConnectionListner {

    void onConnectionReady(TCPConnection tcpConnection);

    void onReceiveString(TCPConnection tcpConnection, String value);

    void onDisconnect(TCPConnection tcpConnection);

    void onExeption(TCPConnection tcpConnection, Exception e);

}
