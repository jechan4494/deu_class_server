package network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class ServerMain {
  private static final int PORT = 9877;
  private static final int MAX_CLIENTS = 3;
  private static final Semaphore semaphore = new Semaphore(MAX_CLIENTS);

  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      System.out.println("서버 시작! 최대 동시 접속자: " + MAX_CLIENTS);

      while (true) {
        semaphore.acquire(); // 최대 동시 접속자 제한
        Socket clientSocket = serverSocket.accept();
        System.out.println("클라이언트 접속! 현재 접속자: " + (MAX_CLIENTS - semaphore.availablePermits()));

        new Thread(() -> {
          try {
            new ClientHandler(clientSocket).run();
          } finally {
            semaphore.release();
            System.out.println("클라이언트 연결 종료! 현재 접속자: " + (MAX_CLIENTS - semaphore.availablePermits()));
          }
        }).start();

        // 또는 아래처럼 바로 스레드에 ClientHandler를 넘겨도 됩니다.
        // new Thread(new ClientHandler(clientSocket)).start();
        // semaphore.release()는 ClientHandler의 finally에서 직접 호출해야 함.
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}