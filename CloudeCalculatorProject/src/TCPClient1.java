import java.io.*;
import java.net.*;

public class TCPClient1 {
    private static final String CONFIG_FILE_PATH = "src/server_info.dat";
    //서버 정보 파일이 없을 경우 - default
    private static final String DEFAULT_SERVER_IP = "localhost";
    private static final int DEFAULT_PORT_NUMBER = 6789;

    public static void main(String argv[]) throws Exception {
        String[] serverInfo = ServerInfoReader.readServerInfo();
        String serverIP = serverInfo[0];
        int portNumber = Integer.parseInt(serverInfo[1]);
        
        Socket clientSocket = null;
        BufferedReader inFromUser = null;
        DataOutputStream outToServer = null;

        try {
            clientSocket = new Socket(serverIP, portNumber);
            inFromUser = new BufferedReader(new InputStreamReader(System.in));
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (true) {
                System.out.print("Enter the operation (e.g., ADD 10 20): ");
                String userMessage = inFromUser.readLine();
                if (userMessage.equalsIgnoreCase("bye")) {
                    System.out.println("Exiting the client.");
                    break;
                }
                outToServer.writeBytes(userMessage + '\n');
                String serverResponse = inFromServer.readLine();
                displayResultOrError(serverResponse);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (clientSocket != null)
                    clientSocket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        
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
