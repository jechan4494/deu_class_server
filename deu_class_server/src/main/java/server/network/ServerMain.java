package server.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class ServerMain {
  private static final int PORT = 9876;
  private static final int MAX_CLIENTS = 3;
  private static final Semaphore semaphore = new Semaphore(MAX_CLIENTS);

  public static void main(String[] args) throws Exception {
    ServerSocket serverSocket = new ServerSocket(PORT);
    System.out.println("서버 시작! 최대 동시 접속자: " + MAX_CLIENTS);

    while (true) {
      semaphore.acquire(); // 3개가 찰 때까지는 통과, 그 이상은 대기
      Socket clientSocket = serverSocket.accept();
      System.out.println("클라이언트 접속! 현재 접속자: " + (MAX_CLIENTS - semaphore.availablePermits()));
      new Thread(() -> {
        try {
          // 클라이언트 처리 코드 (ClientHandler 등)
          // ...
        } finally {
          semaphore.release(); // 클라이언트 연결 종료 시 permit 반환
          System.out.println("클라이언트 연결 종료! 현재 접속자: " + (MAX_CLIENTS - semaphore.availablePermits()));
        }
      }).start();
    }
  }
}
