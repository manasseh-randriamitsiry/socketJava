package org.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            String serverMessage;
            String clientMessage = "";
            while (!clientMessage.equalsIgnoreCase("bye")){
                System.out.print("reponse: ");
                clientMessage = consoleInput.readLine();
                out.println(clientMessage);

                if ((serverMessage = in.readLine()) != null) {
                    System.out.println("Server: " + serverMessage);
                }
            }

            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
