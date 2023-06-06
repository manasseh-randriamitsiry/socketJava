package org.socket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ClientApplication extends JFrame {
    private JTextArea queryTextArea;
    private JButton sendButton;
    private JTable table;

    private Connection connection;
    private Statement statement;

    public ClientApplication() {
        setTitle("Client Application");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        queryTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(queryTextArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendQuery();
            }
        });

        table = new JTable();

        JPanel panel = new JPanel();
        panel.add(scrollPane);
        panel.add(sendButton);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Établir la connexion à la base de donnée
        String databaseName = "ljsa";
        String databaseUser = "root";
        String databasePassword = "123456789";
        String url = "jdbc:mysql://localhost/"+databaseName;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url,databaseUser,databasePassword);
        } catch (Exception e){
           e.printStackTrace();
        }
    }

    private void sendQuery() {
        String query = queryTextArea.getText();

        try {
            boolean isResultSet = statement.execute(query);

            if (isResultSet) {
                ResultSet resultSet = statement.getResultSet();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Créer un modèle de table pour afficher les résultats
                DefaultTableModel tableModel = new DefaultTableModel();
                for (int i = 1; i <= columnCount; i++) {
                    tableModel.addColumn(metaData.getColumnName(i));
                }

                while (resultSet.next()) {
                    Object[] rowData = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        rowData[i - 1] = resultSet.getObject(i);
                    }
                    tableModel.addRow(rowData);
                }

                table.setModel(tableModel);
            } else {
                int updateCount = statement.getUpdateCount();

                if (updateCount >= 0) {
                    showSuccessMessage("Enregistrement(s) modifié(s): " + updateCount);
                } else {
                    showSuccessMessage("Requête exécutée avec succès.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorMessage("Erreur lors de l'exécution de la requête.");
        }
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ClientApplication clientApp = new ClientApplication();
                clientApp.setVisible(true);
            }
        });
    }
}
