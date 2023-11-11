import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer1 {
    public static void main(String[] args) throws Exception {
        int portNumber = 6789;
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server running....");
            ExecutorService pool = Executors.newFixedThreadPool(20);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(new Cal(clientSocket));
            }
        }
    }

    private static class Cal implements Runnable{
        private Socket socket;
        Cal(Socket socket){
            this.socket = socket;
        }
        
        @Override
        public void run(){
            BufferedReader inFromClient = null;
            DataOutputStream outToClient = null;
            try {
                inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outToClient = new DataOutputStream(socket.getOutputStream());
                while (true) {
                    String clientMessage = inFromClient.readLine();
                    System.out.println("FROM CLIENT: " + clientMessage);

                    // 계산기 로직 추가
                    String response = calculate(clientMessage);

                    // 결과 또는 에러를 클라이언트에게 전송
                    outToClient.writeBytes(response + '\n');
                }
            }            
            catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("클라이언트와 채팅 중 오류가 발생했습니다.");
                }
            }
        }
    }
    private static String calculate(String clientMessage) {
        // 간단한 계산기 로직을 추가
        String[] tokens = clientMessage.split("\\s");

        if (tokens.length == 3) {
            try {
                double operand1 = Double.parseDouble(tokens[1]);
                double operand2 = Double.parseDouble(tokens[2]);

                switch (tokens[0]) {
                    case "ADD":
                        return "ANS " + (operand1 + operand2);
                    case "MIN":
                        return "ANS " + (operand1 - operand2);
                    case "MUL":
                        return "ANS " + (operand1 * operand2);
                    case "DIV":
                        // 0으로 나누는 경우 처리
                        if (operand2 != 0) {
                            return "ANS " + (operand1 / operand2);
                        } else {
                            // 에러 반환
                            return "ERR Divide by Zero";
                        }
                    default:
                        // 지원하지 않는 연산일 경우
                        return "ERR Operations not supported";
                }
            } catch (NumberFormatException e) {
                // 피연산자가 잘못된 형식으로 입력된 경우
                return "ERR invalid operand";
            }
        } else {
            // 유효하지 않은 메시지 형식일 경우
            return "ERR invalid message";
        }
    }
}

