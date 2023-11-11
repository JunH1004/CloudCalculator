import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ServerInfoReader {

    private static final String CONFIG_FILE_PATH = "src/server_info.dat";
    private static final String DEFAULT_SERVER_IP = "localhost";
    private static final int DEFAULT_PORT_NUMBER = 6789;

    /**
     * 서버 정보를 파일에서 읽어오는 메서드
     * return 서버 정보를 담은 배열 [0]: 서버 IP, [1]: 포트 번호
     */
    public static String[] readServerInfo() {
        String[] serverInfo = new String[2];

        try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE_PATH))) {
            // 파일에서 서버 정보 읽기
            serverInfo[0] = br.readLine();
            serverInfo[1] = br.readLine();

            // 읽은 정보가 유효하지 않으면 예외 발생
            if (serverInfo[0] == null || serverInfo[1] == null) {
                throw new IOException("유효하지 않은 설정 파일 형식입니다.");
            }
        } catch (IOException e) {
            // 파일을 읽는 중 오류가 발생하면 기본값 사용
            System.out.println("설정 파일에서 서버 정보를 읽는 중 오류가 발생했습니다. 기본값을 사용합니다.");
            serverInfo[0] = DEFAULT_SERVER_IP;
            serverInfo[1] = String.valueOf(DEFAULT_PORT_NUMBER);
        }

        return serverInfo;
    }
}
