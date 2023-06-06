package org.socket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame {
    private JTextArea textArea;
    private JButton sendButton;
    private JTable table;

    public Client() {
        setTitle("Client Application");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendRequest();
            }
        });

        table = new JTable();

        JPanel panel = new JPanel();
        panel.add(scrollPane);
        panel.add(sendButton);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void sendRequest() {
        String request = textArea.getText();

        try {
            Socket socket = new Socket("localhost", 12345);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Envoyer la requête au serveur
            out.println(request);

            // Recevoir la réponse du serveur
            String response = in.readLine();

            // Mettre à jour le tableau avec la réponse du serveur
            // (code à compléter selon la structure de votre données)
            // Example:
            // DefaultTableModel model = (DefaultTableModel) table.getModel();
            // model.addRow(new Object[]{response});

            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Client clientApp = new Client();
                clientApp.setVisible(true);
            }
        });
    }
}
