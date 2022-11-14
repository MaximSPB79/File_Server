package server;

import java.io.*;

public class Main {

    private static final int DEFAULT_PORT = 8186;

    public static void main(String[] args) {

        try {
            MyServer server = new MyServer(DEFAULT_PORT);
            server.start(); // создаем экземпляр сервера и запускаем его
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}