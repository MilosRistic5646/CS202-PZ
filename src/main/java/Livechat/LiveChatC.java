package Livechat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
public class LiveChatC extends JFrame implements ActionListener {
    static Socket conn;
    JPanel panel;
    JTextField NewMsg;
    JTextArea ChatHistory;
    JButton Send;
    public LiveChatC() throws  IOException {
        panel = new JPanel();
        NewMsg = new JTextField();
        ChatHistory = new JTextArea();
        Send = new JButton("Send");
        this.setSize(500, 500);
        this.setVisible(true);
        panel.setLayout(null);
        this.add(panel);
        ChatHistory.setBounds(20, 20, 450, 360);
        panel.add(ChatHistory);
        NewMsg.setBounds(20, 400, 340, 30);
        panel.add(NewMsg);
        Send.setBounds(375, 400, 95, 30);
        panel.add(Send);
        Send.addActionListener(this);
        conn = new Socket(InetAddress.getLocalHost(), 3162);

        ChatHistory.setText("Connected to Server");
        this.setTitle("Client");
        while (true) {
            try {
                DataInputStream dis = new DataInputStream(conn.getInputStream());
                String string = dis.readUTF();
                ChatHistory.setText(ChatHistory.getText() + '\n' + "Operater:"
                        + string);
            } catch (Exception e1) {
                ChatHistory.setText(ChatHistory.getText() + '\n'
                        + "Message sending fail:Network Error");
                try {
                    Thread.sleep(3000);
                    conn.close();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if ((e.getSource() == Send) && (NewMsg.getText() != "")) {
            ChatHistory.setText(ChatHistory.getText() + '\n' + "Me:"
                    + NewMsg.getText());
            try {
                DataOutputStream dos = new DataOutputStream(
                        conn.getOutputStream());
                dos.writeUTF(NewMsg.getText());
            } catch (Exception e1) {
                ChatHistory.setText(ChatHistory.getText() + '\n'
                        + "Message sending fail:Network Error");
                try {
                    Thread.sleep(3000);
                    System.exit(0);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
            NewMsg.setText("");
        }
    }
    public static void main(String[] args) throws UnknownHostException,
            IOException {
        LiveChatC chatForm = new LiveChatC();
    }
}
