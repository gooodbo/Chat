package com.chat.client;

import com.network.com.TCPConnection;
import com.network.com.TCPConnectionListner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ChatWindow extends JFrame implements ActionListener, TCPConnectionListner {

    private static final String IP_ADRESS = "192.168.0.101";
    private static final int PORT = 8888;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private final JTextArea log = new JTextArea();
    private final JTextField fieldNickname = new JTextField("sasha");
    private final JTextField fieldText = new JTextField();


    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                new ChatWindow();
            }

        });

    }


    private TCPConnection connection;

    private ChatWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        log.setEditable(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);
        fieldText.addActionListener(this);
        add(fieldNickname, BorderLayout.NORTH);
        add(fieldText, BorderLayout.SOUTH);

        setVisible(true);

        try {
            connection = new TCPConnection(this, IP_ADRESS, PORT);
        } catch (IOException e) {
            PrintMsg("Connection Exeption " + e);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldText.getText();
        if (msg.equals("")) return;
        fieldText.setText(null);
        connection.sentString(fieldNickname.getText() + ": " + msg);

    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        PrintMsg("Connection ready...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        PrintMsg(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        PrintMsg("Connection close...");

    }

    @Override
    public void onExeption(TCPConnection tcpConnection, Exception e) {
        PrintMsg("Connection Exeption " + e);

    }

    private synchronized void PrintMsg(String msg) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());

            }
        });

    }

}

