import java.io.*;
import java.net.*;

public class TCPClient1 {
    private static final String CONFIG_FILE_PATH = "src/server_info.dat";
    // 서버 정보 파일이 없을 경우 - 기본값
    private static final String DEFAULT_SERVER_IP = "localhost";
    private static final int DEFAULT_PORT_NUMBER = 6789;

    public static void main(String argv[]) throws Exception {
        // 서버 정보를 읽어옴
        String[] serverInfo = ServerInfoReader.readServerInfo();
        String serverIP = serverInfo[0];
        int portNumber = Integer.parseInt(serverInfo[1]);

        Socket clientSocket = null;
        BufferedReader inFromUser = null;
        DataOutputStream outToServer = null;

        try {
            // 서버에 소켓 연결
            clientSocket = new Socket(serverIP, portNumber);
            inFromUser = new BufferedReader(new InputStreamReader(System.in));
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (true) {
                // 사용자로부터 연산 입력 받음
                System.out.print("연산을 입력하세요 (예: ADD 10 20): ");
                String userMessage = inFromUser.readLine();

                // 사용자가 "bye"를 입력하면 클라이언트 종료
                if (userMessage.equalsIgnoreCase("bye")) {
                    System.out.println("클라이언트를 종료합니다.");
                    break;
                }

                // 서버로 메시지 전송
                outToServer.writeBytes(userMessage + '\n');

                // 서버로부터 응답 받음
                String serverResponse = inFromServer.readLine();

                // 결과 또는 에러 출력
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
        // 서버 응답을 공백으로 분리하여 처리
        String[] tokens = serverResponse.split("\\s", 2);

        if (tokens.length >= 2) {
            String command = tokens[0];
            String data = tokens[1];

            // 응답에 따라 결과 또는 에러 출력
            switch (command) {
                case "ANS":
                    System.out.println("결과: " + data);
                    break;
                case "ERR":
                    System.out.println("에러 메시지: " + data);
                    break;
                default:
                    System.out.println("알 수 없는 응답 형식: " + serverResponse);
            }
        } else {
            System.out.println("알 수 없는 응답 형식: " + serverResponse);
        }
    }
}
