import java.io.*;
import java.net.*;

public class TcpClient {
    public static void main(String[] args) throws IOException {

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(args[0], Integer.parseInt(args[1]));
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + args[0] );
            System.exit(1);
        }

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String userInput;

		// send data without CR/LF
		OutputStream rawout = echoSocket.getOutputStream();
		InputStream rawin = echoSocket.getInputStream();
		//String s = "abcde";
		//rawout.write( s.getBytes() );
		byte[] b = new byte[200];
		//int rlen = rawin.read( b, 0, 100 );
		//s = new String( b, 0, rlen );
		//System.out.println("echo: " + s);

		// process one line of text
		while ( true ) {
			System.out.print("Type string or QUIT: ");
			userInput = stdIn.readLine();
			if ( userInput == null || userInput.equals("QUIT") )
				break;
			
			// send it to the server (with newline at end)
			//out.println(userInput);
			// get the response
			//System.out.println("echo: " + in.readLine());
			
			// send it to the server (no newline at end)
			rawout.write( userInput.getBytes() );
			// get the response from the server
			int rlen = rawin.read( b, 0, 200 );
			String s = new String( b, 0, rlen );
			System.out.println("echo: " + s);
		}

		out.close();
		in.close();
		stdIn.close();
		echoSocket.close();
    }
}
