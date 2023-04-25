import java.io.*;
import java.net.*;


public class Client {
    private static final String HOST = "localhost"; // Server hostname
    private static final int PORT = 19999; // Server port
    
    public static void main(String[] args) {
        try {
            Socket socket1 = new Socket(HOST, PORT);
            String userChoice;

            // Create input and output streams for the server
            PrintWriter out = new PrintWriter(socket1.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket1.getInputStream()));

            // Get client name from user
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Please enter your name: ");
            String clientName = input.readLine();
            out.println(clientName);

            // Receiving acknowledgement from server
            String response = in.readLine();
            System.out.println(response);

            // Loop to send requests to server
            
            while (true) {
                System.out.print("Choose from the following...\nOptions: (ADD/SUBTRACT/MULTIPLY/DIVIDE/QUIT): ");
                userChoice = input.readLine();

                // Check if user wants to close connection
                if (userChoice.equalsIgnoreCase("QUIT")) {
                    out.println(userChoice);
                    in.close();
                    out.close();
                    socket1.close();
                    System.out.println("Closing connection.");
                    System.exit(0);
                    break;
                }

                out.println(userChoice);

                // Receive response from server
                response = in.readLine();
                System.out.println(response);

            }

            //Closing resources
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}