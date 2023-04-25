import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 19999; // Server port
    // List to keep track of client writers
    private static ArrayList<PrintWriter> clientWriters = new ArrayList<>(); 
    // Map to keep track of logged-in users
    private static HashMap<String, Date> loggedInUsers = new HashMap<>(); 

    public static void main(String[] args) {
        try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			System.out.println("Math Server is running on port " + PORT);
			
			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Client connected: " + clientSocket);

				// Create a new thread to handle the client connection
				ClientHandler clientHandler = new ClientHandler(clientSocket);
				clientHandler.start();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private String name_of_client;
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
                // Create input and output streams for the client
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // Get client name from client
                name_of_client = in.readLine();
                System.out.println("Client attached: " + name_of_client);

                // Add client writer to the list
                clientWriters.add(out);

                // Send acknowledgement to client
                out.println("Connection successful. Welcome, " + name_of_client + "!");

                // Add client login time to the logged-in users map
                loggedInUsers.put(name_of_client, new Date());

                // Loop to wait for client requests
                String request;
                while ((request = in.readLine()) != null) {
                    System.out.println("Request from " + name_of_client + ": " + request);

                    // Check if user wants to close connection
                    if (request.equalsIgnoreCase("QUIT")) {
                        System.out.println("Client disconnected: " + name_of_client);
                        
                        // Close resources for this client
                        in.close();
                        out.close();
                        clientSocket.close();

                        // Remove client writer from the list
                        clientWriters.remove(out);

                        // Remove client from logged-in users map
                        loggedInUsers.remove(name_of_client);
                        
                        break;
                    }

                    // Process client request
                    String response = process(request);
                    
                    // Send response to client
                    out.println(response);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String process(String request) {
            //processing client request, parsing the space in between the choices
            //ex request "ADD 4 5"
            String[] broken_down = request.split(" ");
            String operation = broken_down[0];
            int num1 = Integer.parseInt(broken_down[1]);
            int num2 = Integer.parseInt(broken_down[2]);
            int result = 0;

            switch (operation) {
                case "ADD":
                    result = num1 + num2;
                    break;
                case "SUBTRACT":
                    result = num1 - num2;
                    break;
                case "MULTIPLY":
                    result = num1 * num2;
                    break;
                case "DIVIDE":
                    if (num2 != 0) {
                        result = num1 / num2;
                    } else {
                        return "Error: Cannot divide by zero";
                    }
                    break;
                default:
                    return "Error: Invalid operation";
            }

            return "Result: " + result;
        }
    }
}
