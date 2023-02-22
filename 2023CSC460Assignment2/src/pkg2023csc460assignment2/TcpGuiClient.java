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


//	boolean isConnected = false;		// first typing should be host:port
    static Socket echoSocket = null;
    OutputStream rawout = null;			// write end of the socket
    InputStream rawin = null;
    int isLoggedIn = 0;

    public TcpGuiClient() {
        super(new GridBagLayout());

        // create a mini-layout for the host and port
        JPanel loginPanel = new JPanel();
        FlowLayout fLayout = new FlowLayout(FlowLayout.LEFT);
        loginPanel.setLayout(fLayout);

         String host = "localhost";
                // get port number
                int port = 4269;

                // actually make the connection
//                makeConnection(host, port);
//
//                updateEnable();
        
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
                login_request(tfUser.getText(),tfPass.getText());
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

    

    // send data to the server and get the response
//    public void sendData() {
//        // not connected?
//        if (echoSocket == null || !echoSocket.isConnected()) {
//            return;
//        }
//
//        try {
//            // what did they type?
//            String userData = tfSendData.getText();
//
//            // clear the input text field
//            tfSendData.setText("");
//
//            // and move cursor there
//            tfSendData.requestFocus();
//
//            // send it to the server (no newline at end)
//            rawout.write(userData.getBytes(StandardCharsets.UTF_8));
//
//            // get the response from the server
//            int rlen = rawin.read(b, 0, BUF_LEN - 1);
//            String str = new String(b, 0, rlen, StandardCharsets.UTF_8);
//
//            // display the incoming data
//            taReceivedData.append(str + "\n");
//        } catch (IOException e) {
//            closeSocket();
//        }
//    }

    public void login_request(String username, String pass) {

        byte[] id;
        byte[] password;
        byte[] id_len = new byte[2];
        byte[] pw_len = new byte[2];

        try {
            id = username.getBytes("UTF-8");
            password = pass.getBytes();
            int int_id_len = id.length;
            String hex_id_len = Integer.toHexString(int_id_len);
            id_len = hex_id_len.getBytes("UTF-8");
            int int_pw_len = password.length;
            String hex_pw_len = Integer.toHexString(int_pw_len);
            pw_len = (hex_pw_len.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return;
        }

        ByteBuffer b = ByteBuffer.allocate(2+id.length+password.length+4); 
        System.out.println("made it to concat");
        b.putInt(0x10);
        System.out.println("0x10");
        b.put(id_len);
        System.out.println(new String(id_len, StandardCharsets.UTF_8));
        b.put(id);
        System.out.println(new String(id, StandardCharsets.UTF_8));
        b.put(pw_len);
        System.out.println(new String(pw_len, StandardCharsets.UTF_8));
        b.put(password);
        System.out.println(new String(password, StandardCharsets.UTF_8));
        byte[] protocol_byte = new byte[b.remaining()];
        b.get(protocol_byte);
        byte[] result;
        try {
            rawout.write(protocol_byte);
            int rlen = rawin.read( result, 0, 65535 );
            String result_string = new String( result, 0, rlen, "UTF-8" );
            
        } catch ( IOException e ) {
			closeSocket();
		}
    }
    // update enabled/disabled buttons based on connection
    public void updateEnable() {
        // are we connected?
        if (echoSocket != null && echoSocket.isConnected() && isLoggedIn == 1) {
            // connected and ready
            btnSend.setEnabled(true);
            btnLogin.setEnabled(false);
            btnLogout.setEnabled(true);
        } else if (echoSocket != null && echoSocket.isConnected() && isLoggedIn == 0){
            // not connected
            btnSend.setEnabled(false);
            btnLogin.setEnabled(true);
            btnLogout.setEnabled(false);
        }
        else{
            btnSend.setEnabled(false);
            btnLogin.setEnabled(false);
            btnLogout.setEnabled(false);
        }
    }

    // update button enabled/disabled
    // establish a link to the server
    public void makeConnection(String host, int port) {

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

                // set host and port if provided on command line
//                if (args.length == 2) {
//                    tfUser.setText(args[0]);
//                    tfPass.setText(args[1]);
//                }
            }
        });
    }
}
