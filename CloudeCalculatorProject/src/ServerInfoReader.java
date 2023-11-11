import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ServerInfoReader {

    private static final String CONFIG_FILE_PATH = "src/server_info.dat";
    private static final String DEFAULT_SERVER_IP = "localhost";
    private static final int DEFAULT_PORT_NUMBER = 6789;

    public static String[] readServerInfo() {
        String[] serverInfo = new String[2];

        try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE_PATH))) {
            serverInfo[0] = br.readLine();
            serverInfo[1] = br.readLine();

            if (serverInfo[0] == null || serverInfo[1] == null) {
                throw new IOException("Invalid configuration file format.");
            }
        } catch (IOException e) {
            System.out.println("Error reading server information from the file. Using default values.");
            serverInfo[0] = DEFAULT_SERVER_IP;
            serverInfo[1] = String.valueOf(DEFAULT_PORT_NUMBER);
        }

        return serverInfo;
    }
}
