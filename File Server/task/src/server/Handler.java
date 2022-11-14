package server;


import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Handler {
    private MyServer myServer;
    private Socket clientSocket;
    private String path = "C:\\java\\File Server\\File Server\\task\\src\\server\\data\\";
    private String fileContent;
    private DataOutputStream out;
    private DataInputStream in;
    private String responseCode;
    private File file;
    private String fileName;

    public Handler(MyServer myServer, Socket clientSocket) {
        this.myServer = myServer;
        this.clientSocket = clientSocket;
    }

    public void handle() throws IOException {
        // для клиента создаем потоки входа и выхода данных
        out = new DataOutputStream(clientSocket.getOutputStream());
        in = new DataInputStream(clientSocket.getInputStream());
    }

    public void commandProcessing() {
        try {
            String command = in.readUTF();
            if ("exit".equals(command)) {
                System.exit(1);
            }
            String[] request = command.split(" ", 3);
            if (request.length >= 3) {
                fileContent = request[2];
            }
            fileName = request[1];
            file = new File(path + fileName);
            switch (request[0]) {
                case "GET":
                    methodGet();
                    break;
                case "PUT":
                    addFile();
                    break;
                case "DELETE":
                    methodDelete();
                    break;
                default:
                    System.out.println("No such command");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addFile() {
        try {
            if (file.createNewFile()) {
                PrintWriter pw = new PrintWriter(file);
                pw.println(fileContent);
                pw.close();
                responseCode = "200";
            } else {
                responseCode = "403";
            }
            out.writeUTF(responseCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void methodDelete() throws IOException {
        if (file.delete()) {
            responseCode = "201";
        } else {
            responseCode = "404";
        }
        out.writeUTF(responseCode);
    }

    private void methodGet() throws IOException {
        if (file.exists()) {
            Scanner scanner = new Scanner(file);
            String content = scanner.nextLine();
            responseCode = "200 " + content;
            scanner.close();
        } else {
            responseCode = "404";
        }
        out.writeUTF(responseCode);
    }
}



