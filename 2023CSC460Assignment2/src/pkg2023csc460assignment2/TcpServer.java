import java.net.*;
import java.io.*;
import java.lang.Thread;
import java.util.Enumeration;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TcpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        boolean listening = true;
		int clientCount = 0;

		// verify we have a port #
		if ( args.length != 1 ) {
			System.out.println("Error: No port # provided!");
			System.exit(0);
		}
		
		// display the IP address(es) of the server
		Enumeration e = NetworkInterface.getNetworkInterfaces();
		while(e.hasMoreElements())
		{
			NetworkInterface n = (NetworkInterface) e.nextElement();
			Enumeration ee = n.getInetAddresses();
			while (ee.hasMoreElements())
			{
				InetAddress i = (InetAddress) ee.nextElement();
				// don't worry about link local or loopback addrs
				if ( i.isLinkLocalAddress() || i.isLoopbackAddress() )
					continue;
				System.out.println( "Local IP Addr: " + i.getHostAddress());
			}
		}

		// start a thread to listen for a keypress
		System.out.println("Press Enter to quit");
		new TcpServerKeyWait().start();
		
        try {
            serverSocket = new ServerSocket(Integer.parseInt(args[0]));
        } catch (IOException ex) {
            System.err.println("Could not listen on port: "+args[0] );
            System.exit(-1);
        }

        while (listening)
		{
			// note a new client
			clientCount++;
			
			// start a client thread
            new TcpServerThread(serverSocket.accept(), clientCount).start();

			// debugging output
			System.out.println("Started new thread, client " + clientCount);
		}

        serverSocket.close();
    }
}

// thread to listen for the user to press enter on the keyboard
class TcpServerKeyWait extends Thread {
	public void run() {
		try {
			// wait for anything
			int c = System.in.read();
		} catch ( IOException e ) {
		}
		
		// done
		System.exit(0);
	}
}

class TcpServerThread extends Thread {
    private Socket socket = null;
	private int clientNumber = 0;

    public TcpServerThread(Socket socket, int cno) {
        super("EchoServerThread");
		this.socket = socket;
		clientNumber = cno;				// remember who this client is
    }

    public void login() {
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();

			while ( true )
			{
				byte[] userdata = new byte[200];

				// read up to 200 chars from the input
				int chars_read = in.read( userdata, 0, 200 );

				// end of file?
				if ( chars_read < 0 )
					break;					// yes, at EOF

				// convert to a string to make uppercase easier
				String str = new String( userdata, 0, chars_read );
				System.out.println("Client " + clientNumber + " Received " + str );
				
				// send it back to the client
				userdata = str.toUpperCase().getBytes();
				out.write( userdata );
			}
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Thread exiting, client " + clientNumber);
	}
    
    public int displayCurMembs(int chatNum) {return 1;}
    
    public void sendWelcome(){}
    
    public void userJoined(){}
    
    public void userLeft(){}
    
    public void cantJoin(){}
    
    public void invalidChatroom(){}
    
    
}