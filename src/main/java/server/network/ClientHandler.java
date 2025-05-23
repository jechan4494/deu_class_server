import model.User;
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
  private final Socket socket;

  public ClientHandler(Socket socket) {
    this.socket = socket;
  }

  public void run() {
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    try {
      System.out.println("서버: OOS 생성");
      oos = new ObjectOutputStream(socket.getOutputStream());
      System.out.println("서버: OIS 생성");
      ois = new ObjectInputStream(socket.getInputStream());
      System.out.println("서버: 스트림 생성 완료");

      String command = (String) ois.readObject();
      System.out.println("서버: 명령 수신 = " + command);

      if ("register".equals(command)) {
        User user = (User) ois.readObject();
        System.out.println("서버: 회원가입 요청 user = " + user);
        boolean success = UserController.registerUser(user);
        oos.writeObject(success ? "success" : "duplicate");
        oos.flush();
        System.out.println("서버: 회원가입 응답 전송");
      } else if ("login".equals(command)) {
        String id = (String) ois.readObject();
        String pw = (String) ois.readObject();
        System.out.println("서버: 로그인 요청 id = " + id + ", pw = " + pw);
        User user = UserController.login(id, pw);
        oos.writeObject(user);
        oos.flush();
        System.out.println("서버: 로그인 응답 전송");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try { if (ois != null) ois.close(); } catch (Exception ignore) {}
      try { if (oos != null) oos.close(); } catch (Exception ignore) {}
      try { socket.close(); } catch (Exception ignore) {}
      System.out.println("서버: 소켓 및 스트림 종료");
    }
  }
}
