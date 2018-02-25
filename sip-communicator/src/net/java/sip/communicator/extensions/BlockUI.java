package net.java.sip.communicator.extensions;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import net.java.sip.communicator.common.Utils;
import net.java.sip.communicator.gui.GuiManager;
import net.java.sip.communicator.sip.security.UserCredentials;

public class BlockUI extends JFrame {
    private ConnectToDB database;

    public static void main(String[] args) {
        new BlockUI();
    }

    public BlockUI() {
        database = new ConnectToDB();
        JFrame frame = new JFrame("Block User");
        frame.setVisible(true);
        frame.setSize(400, 100);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

        JPanel panel = new JPanel();
        frame.add(panel);

        JLabel label = new JLabel("Type Username");
        panel.add(label);

        JTextField txtfld = new JTextField();
        txtfld.setPreferredSize(new Dimension(200, 25));
        panel.add(txtfld);

        JButton okbtn = new JButton("OK");
        okbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // g07: Get username from configurations xml file
                String username = Utils.getProperty("net.java.sip.communicator.sip.USER_NAME");
                String textFieldValue = txtfld.getText();
                // g07: Update database
                Connection conn = database.start();
                String sql = "INSERT INTO blocks VALUES (?,?)";
                PreparedStatement stmt;
                try {
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, username);
                    stmt.setString(2, textFieldValue);
                    stmt.executeUpdate();
                    stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                database.close(conn);

                frame.dispose();

            }
        });
        
        panel.add(okbtn);

        // pack();
    }
}
