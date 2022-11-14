package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient implements Serializable {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 8186;
    private static Scanner scanner;
    private static String command;
    private static String fileMame;
    private static String commands;
    private static String content;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            sendingDataToTheServer(output); // отправляем данные на сервер
            serverResponseProcessing(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void sendingDataToTheServer(DataOutputStream output) throws IOException {
        System.out.println("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");
        scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        switch (str) {
            case "1":
                command = "GET";
                break;
            case "2":
                command = "PUT";
                break;
            case "3":
                command = "DELETE";
                break;
            case "exit":
                output.writeUTF(str);
                System.exit(1);
                break;
        }
        switch (command) {
            case "GET":
            case "DELETE":
                inputCommands(output);
                break;
            case "PUT":
                System.out.print("Enter filename: ");
                fileMame = scanner.nextLine();
                System.out.print("Enter file content: ");
                String fileContent = scanner.nextLine();
                commands = command + " " + fileMame + " " + fileContent;
                output.writeUTF(commands);
                break;
        }
    }

    private static void inputCommands(DataOutputStream output) throws IOException {
        System.out.print("Enter filename: ");
        fileMame = scanner.nextLine();
        commands = command + " " + fileMame;
        scanner.close();
        output.writeUTF(commands); // отправляем команду на сервер
    }

    private static void serverResponseProcessing(DataInputStream input) throws IOException {
        String serverResponse = input.readUTF(); // принимаем сообщение от сервера
        String[] responseParse = serverResponse.split(" ", 2);
        String responseCommand = responseParse[0];
        if (responseParse.length >= 2) {
            content = responseParse[1];
        }
        switch (responseCommand) {
            case "200":
                if (responseParse.length == 1) {
                    System.out.println("The request was sent.\n" +
                            "The response says that file was created!");
                } else {
                    System.out.println("The request was sent.\n" +
                            "The content of the file is: " + content);
                }
                break;
            case "403":
                System.out.println("The request was sent.\n" +
                        "The response says that creating the file was forbidden!");
                break;
            case "404":
                System.out.println("The request was sent.\n" +
                        "The response says that the file was not found!");
                break;
            case "201":
                System.out.println("The request was sent.\n" +
                        "The response says that the file was successfully deleted!");
                break;
        }
    }
}
