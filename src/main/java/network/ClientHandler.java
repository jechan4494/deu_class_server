package network;

import controller.UserController;
import model.User;
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
  private final Socket socket;

  public ClientHandler(Socket socket) {
    this.socket = socket;
  }

  public void run() {
    try (
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())
    ) {
      String command = (String) ois.readObject();
      if (command.equals("register")) {
        User user = (User) ois.readObject();
        boolean success = UserController.registerUser(user);
        oos.writeObject(success ? "success" : "duplicate");
      } else if (command.equals("login")) {
        String id = (String) ois.readObject();
        String pw = (String) ois.readObject();
        User user = UserController.login(id, pw);
        oos.writeObject(user); // null이면 로그인 실패
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}