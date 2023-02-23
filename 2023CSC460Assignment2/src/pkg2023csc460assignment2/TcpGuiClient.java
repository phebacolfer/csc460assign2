package pkg2023csc460assignment2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.lang.Thread;
import java.lang.*;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class TcpGuiClient extends JPanel {

    protected JTextField tfSendData;
    protected JTextField tfSearchData;
    protected JTextArea taReceivedData;
    static protected JTextField tfUser;
    static protected JTextField tfPass;
    protected JButton btnLogin;
    protected JButton btnLogout;
    protected JButton btnSend;
    protected JButton btnChatSearch;
    protected JButton btnChatJoin;
    protected JButton btnChatLeave;

//	boolean isConnected = false;		// first typing should be host:port
    static Socket echoSocket = null;
    OutputStream rawout = null;			// write end of the socket
    InputStream rawin = null;
    int isLoggedIn = 0;
    int isInRoom = 0;
    final String host = "localhost";
    final int port = 4269;

    public TcpGuiClient() {
        super(new GridBagLayout());

        // create a mini-layout for the host and port
        JPanel loginPanel = new JPanel();
        FlowLayout fLayout = new FlowLayout(FlowLayout.LEFT);
        loginPanel.setLayout(fLayout);

        // actually make the connection
//                
        btnLogin = new JButton("Login");
        //btnLogin.addActionListener(this);

        loginPanel.add(btnLogin);

        btnLogout = new JButton("Logout");
        //btnLogin.addActionListener(this);

        btnLogout.setEnabled(false);
        loginPanel.add(btnLogout);

        JLabel lblUser = new JLabel("Username");
        loginPanel.add(lblUser);

        tfUser = new JTextField("Username", 21);
        loginPanel.add(tfUser);

        JLabel lblPass = new JLabel("Password");
        loginPanel.add(lblPass);

        tfPass = new JTextField("Password", 21);
        loginPanel.add(tfPass);

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;
        add(loginPanel, c);

        //panel for chatroom searching
        JPanel searchPanel = new JPanel();
        FlowLayout fLayout2 = new FlowLayout(FlowLayout.LEFT);
        searchPanel.setLayout(fLayout2);

        btnChatSearch = new JButton("Search");
        btnChatSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //put searching for chatrooms stuff here
                //sendData();
            }
        });
        btnChatSearch.setEnabled(false);
        searchPanel.add(btnChatSearch);

        // create a place for the user to enter a message to send
        tfSearchData = new JTextField(60);
        // respond if they pressed enter
        tfSearchData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // user typed something
                //sendData();
            }
        });
        searchPanel.add(tfSearchData);

        btnChatJoin = new JButton("Join");
        btnChatJoin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //put searching for chatrooms stuff here
                //sendData();
            }
        });
        btnChatJoin.setEnabled(false);
        searchPanel.add(btnChatJoin);

        btnChatLeave = new JButton("Leave");
        btnChatLeave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //put searching for chatrooms stuff here
                //sendData();
            }
        });
        btnChatLeave.setEnabled(false);
        searchPanel.add(btnChatLeave);

        //Add Components to this panel.
        //GridBagConstraints c = new GridBagConstraints();
        //c.gridwidth = GridBagConstraints.REMAINDER;
        //c.fill = GridBagConstraints.HORIZONTAL;
        add(searchPanel, c);

        //panel for search results
        // panel for incoming data
        taReceivedData = new JTextArea(30, 80);
        taReceivedData.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taReceivedData);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);

        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(fLayout2);

        btnSend = new JButton("Send Chat");
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // user typed something
                //sendData();
            }
        });
        btnSend.setEnabled(false);

        //btnLogin.addActionListener(this);
        sendPanel.add(btnSend);

        // create a place for the user to enter a message to send
        tfSendData = new JTextField(60);
        // respond if they pressed enter
        tfSendData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // user typed something
                //sendData();
            }
        });
        sendPanel.add(tfSendData);

        //Add Components to this panel.
        //GridBagConstraints c = new GridBagConstraints();
        //c.gridwidth = GridBagConstraints.REMAINDER;
        //c.fill = GridBagConstraints.HORIZONTAL;
        add(sendPanel, c);

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //String username = lblUser.getText();
                login_request(tfUser.getText(), tfPass.getText());
                tfPass.setText("");
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

//                //put logout stuff here
            }
        });

        //textArea.setText("Connect by typing host:port\n");
    }

    
    
    
    
    public void join_room(String roomName){
        // join is 0x11
        ByteBuffer b = ByteBuffer.allocate(6);
        b.put((byte) 0x11);
        byte[] protocol_byte = b.array();
        byte[] fullResponse = new byte[8];
        try {
            byte[] room = roomName.getBytes();
            byte[] responseCode;
            byte[] welcomeLength;
            byte[] welcomeMessage;
            b.put(room);
            protocol_byte = b.array();
            rawout.write(protocol_byte);
            rawin.read(fullResponse, 0, 8);
            //get resulting string as byte array?
            // get the response from the server
            rawin.read(responseCode, 0, 2);
//Reads up to len bytes of data from the input stream into an array of bytes.
			int rlen = rawin.read( b, 0, BUF_LEN-1 );
			String str = new String( b, 0, rlen, StandardCharsets.UTF_8 );
					
			// display the incoming data
			taReceivedData.append(str + "\n");
           // byte[] response = Arrays.copyOfRange(byte[] original, int from, int to);
            //0x82 is valid login, 0xC2 is invalid user or password, any other error
            switch (Integer.parseInt(result_string)) {
                case 0x80:
                    taReceivedData.setText("Chatroom join successful");
                    //do more stuff here
                    break;
                case 0xC0:
                    taReceivedData.setText("Chatroom full, could not join");
                    break;
                case 0xC7:
                    taReceivedData.setText("Chatroom name invalid, could not join");
                    break;
                case 0xC1:
                    taReceivedData.setText("User not logged in, could not join");
                    break;
                default:
                    taReceivedData.setText("Something unexpected happened, please try again");
                    break;
            }
        } catch (IOException e) {
            taReceivedData.setText("Join room unsuccessful, please try again");
        }
        updateEnable();
    }
    
    public void logout_request() {

        ByteBuffer b = ByteBuffer.allocate(2);
        b.put((byte) 0x14);
        byte[] protocol_byte = b.array();
        try {
            rawout.write(protocol_byte);
        } catch (IOException e) {
            taReceivedData.setText("Logout unsuccessful, please try again");
        }
        updateEnable();
    }
    
    public void closeSocket() {
        try {
            // close the socket
            rawin.close();
            rawout.close();
            echoSocket.close();
            // don't need socket any longer
            echoSocket = null;

            updateEnable();
        } catch (IOException e) {
            // ignore it
        }
    }

    public void login_request(String username, String pass) {

        makeConnection();
        byte[] id;
        byte[] password;
        ByteBuffer id_len = ByteBuffer.allocate(2);
        ByteBuffer pw_len = ByteBuffer.allocate(2);
        id_len.order(ByteOrder.BIG_ENDIAN);
        pw_len.order(ByteOrder.BIG_ENDIAN);

        try {
            id = username.getBytes("UTF-8");
            password = pass.getBytes("UTF-8");
            int int_id_len = id.length;
            id_len.putShort((short) int_id_len);
            int int_pw_len = password.length;
            pw_len.putShort((short) int_pw_len);
        } catch (UnsupportedEncodingException e) {
            return;
        }

        ByteBuffer b = ByteBuffer.allocate(2 + id.length + password.length + 4);
        b.put((byte) 0x10);
        System.out.println(((byte) 0x10));
        b.put(id_len);
        b.put(id);
        b.put(pw_len);
        b.put(password);
        System.out.println(((byte) 0x82));
        byte[] protocol_byte = b.array();
        byte[] result = null;
        try {
            rawout.write(protocol_byte);
            rawin.read(result, 0, 2);
            //get resulting byte array as string?
            String result_string = new String(result, 0, result.length, "UTF-8");
            //0x82 is valid login, 0xC2 is invalid user or password, any other error
            switch (Integer.parseInt(result_string)) {
                case 0x82:
                    isLoggedIn = 1;
                    taReceivedData.setText("Login successful");
                    break;
                case 0xC2:
                    taReceivedData.setText("Username or Password is incorrect, please try again");
                    break;
                default:
                    taReceivedData.setText("Something unexpected happened, please try again");
                    break;
            }

        } catch (IOException e) {
            closeSocket();
        }
        updateEnable();
    }

    // update enabled/disabled buttons based on connection
    public void updateEnable() {
        // are we connected?
        if (echoSocket != null && echoSocket.isConnected() && isLoggedIn == 1 && isInRoom == 1) {
            // connected and ready
            btnSend.setEnabled(true);
            btnLogin.setEnabled(false);
            btnLogout.setEnabled(true);
            btnChatJoin.setEnabled(true);
            btnChatLeave.setEnabled(true);
            btnChatSearch.setEnabled(true);

        } else if (echoSocket != null && echoSocket.isConnected() && isLoggedIn == 0) {
            // not connected
            btnSend.setEnabled(false);
            btnLogin.setEnabled(true);
            btnLogout.setEnabled(false);
            btnChatJoin.setEnabled(false);
            btnChatLeave.setEnabled(false);
            btnChatSearch.setEnabled(false);

        } else if (echoSocket != null && echoSocket.isConnected() && isLoggedIn == 1 && isInRoom == 0) {
            btnSend.setEnabled(false);
            btnLogin.setEnabled(false);
            btnLogout.setEnabled(true);
            btnChatJoin.setEnabled(true);
            btnChatLeave.setEnabled(false);
            btnChatSearch.setEnabled(true);

        } else {
            btnSend.setEnabled(false);
            btnLogin.setEnabled(true);
            btnLogout.setEnabled(false);
            btnChatJoin.setEnabled(false);
            btnChatLeave.setEnabled(false);
            btnChatSearch.setEnabled(false);
        }
    }

    // update button enabled/disabled
    // establish a link to the server
    public void makeConnection() {

        try {
            // connect to the server
            echoSocket = new Socket(host, port);

            // prepare output
            rawout = echoSocket.getOutputStream();
            rawin = echoSocket.getInputStream();

            taReceivedData.append("Connected\n");
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + host);
            System.exit(1);
        }
    }

    
    
    
    
    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TcpGuiClient");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add contents to the window.
        frame.add(new TcpGuiClient());

        //Display the window.
        frame.pack();
        frame.setVisible(true);

        // close socket if they press exit
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    if (echoSocket != null) {
                        echoSocket.close();
                    }
                } catch (IOException ie) {
                }
            }
        });
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
