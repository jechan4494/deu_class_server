package controller;

import model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.util.*;

public class UserController {
  private static final String FILE_PATH = "users.json";

  public static synchronized boolean registerUser(User user) {
    List<User> users = getAllUsers();
    for (User u : users) {
      if (u.getId().equals(user.getId())) {
        return false; // 아이디 중복
      }
    }
    users.add(user);
    saveAllUsers(users);
    return true;
  }

  public static synchronized User login(String id, String password) {
    for (User u : getAllUsers()) {
      if (u.getId().equals(id) && u.getPassword().equals(password)) {
        return u;
      }
    }
    return null;
  }

  public static List<User> getAllUsers() {
    try (Reader reader = new FileReader(FILE_PATH)) {
      List<User> users = new Gson().fromJson(reader, new TypeToken<List<User>>(){}.getType());
      return users == null ? new ArrayList<>() : users;
    } catch (IOException e) {
      return new ArrayList<>();
    }
  }

  private static void saveAllUsers(List<User> users) {
    try (Writer writer = new FileWriter(FILE_PATH)) {
      new Gson().toJson(users, writer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}