package com.isusdlc.ui.mainmenu;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class IRCHandler implements Runnable {

    public interface IRCListener {
        void onMessage(String line);
        void onDisconnect(String reason);
        void onConnect();
    }

    private final String host;
    private final int port;
    private final String nickname;
    private final IRCListener listener;
    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private volatile boolean running;

    public IRCHandler(String host, int port, String nickname, IRCListener listener) {
        this.host = host;
        this.port = port;
        this.nickname = nickname;
        this.listener = listener;
    }

    public void connect() {
        running = true;
        new Thread(this, "irc-client").start();
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, port);
            socket.setTcpNoDelay(true);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            listener.onConnect();

            sendLine("HELLO|" + nickname);

            String line;
            while (running && (line = reader.readLine()) != null) {
                listener.onMessage(line);
            }
        } catch (IOException e) {
            if (running) {
                listener.onDisconnect("Ошибка: " + e.getMessage());
            }
        } finally {
            disconnect();
        }
    }

    public void sendMessage(String text) {
        if (text == null || text.isEmpty()) return;
        sendLine("MSG|" + text);
    }

    public void sendLine(String line) {
        if (writer == null) return;
        synchronized (writer) {
            try {
                writer.write(line);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                listener.onDisconnect("Ошибка отправки");
            }
        }
    }

    public void disconnect() {
        running = false;
        sendLine("QUIT");
        try {
            if (socket != null) socket.close();
        } catch (IOException e) { }
        try {
            if (writer != null) writer.close();
        } catch (IOException e) { }
        try {
            if (reader != null) reader.close();
        } catch (IOException e) { }
    }
}
