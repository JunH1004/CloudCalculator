import java.io.*;
import java.net.*;

public class TCPClient1 {
    private static final String CONFIG_FILE_PATH = "src/server_info.dat";
    private static final String DEFAULT_SERVER_IP = "localhost";
    private static final int DEFAULT_PORT_NUMBER = 6789;

    public static void main(String argv[]) throws Exception {
        // Read server information from the configuration file
            String[] serverInfo = readServerInfo();
            String serverIP = serverInfo[0];
            int portNumber = Integer.parseInt(serverInfo[1]);
            Socket clientSocket = null;
            BufferedReader inFromUser = null;
            DataOutputStream outToServer = null;
            try{
                clientSocket = new Socket(serverIP, portNumber);
                inFromUser = new BufferedReader(new InputStreamReader(System.in));
                outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (true) {
                    System.out.print("Enter the operation (e.g., ADD 10 20): ");
                    String userMessage = inFromUser.readLine();
                    outToServer.writeBytes(userMessage + '\n');

                    String serverResponse = inFromServer.readLine();
                    displayResultOrError(serverResponse);
                }
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            } 
            finally {
                try {
                    if (clientSocket != null)
                        clientSocket.close(); // 클라이언트 소켓 닫기
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        
    }

    private static String[] readServerInfo() {
        String[] serverInfo = new String[2];

        try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE_PATH))) {
            // Read the server IP and port from the file
            serverInfo[0] = br.readLine(); // Assumes the first line is the server IP
            serverInfo[1] = br.readLine(); // Assumes the second line is the port number

            if (serverInfo[0] == null || serverInfo[1] == null) {
                throw new IOException("Invalid configuration file format.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // If the file doesn't exist or there is an error reading it, use default values
            System.out.println("Using default server information.");
            serverInfo[0] = DEFAULT_SERVER_IP;
            serverInfo[1] = String.valueOf(DEFAULT_PORT_NUMBER);
        }

        return serverInfo;
    }

    private static void displayResultOrError(String serverResponse) {
        String[] tokens = serverResponse.split("\\s", 2);

        if (tokens.length >= 2) {
            String command = tokens[0];
            String data = tokens[1];

            switch (command) {
                case "ANS":
                    System.out.println("Result: " + data);
                    break;
                case "ERR":
                    System.out.println("Error Message: " + data);
                    break;
                default:
                    System.out.println("Unknown response format: " + serverResponse);
            }
        } else {
            System.out.println("Unknown response format: " + serverResponse);
        }
    }
}
