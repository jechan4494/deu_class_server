package model.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {
    private static final String DEFAULT_USER_FILE = "users.json";
    private final String userFile;
    private final Gson gson;
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    public UserDAO() {
        this(DEFAULT_USER_FILE);
    }

    public UserDAO(String userFile) {
        this.userFile = userFile;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        initializeFile();
    }

    private void initializeFile() {
        try {
            File file = new File(userFile);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                List<User> initialUsers = new ArrayList<>();
                initialUsers.add(new User("student", "1234", "홍길동", "컴퓨터공학과", "STUDENT"));
                initialUsers.add(new User("prof", "1234", "김교수", "전자공학과", "PROFESSOR"));
                try (Writer writer = new FileWriter(file)) {
                    new Gson().toJson(initialUsers, writer);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error initializing user file: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        return loadUsers();
    }

    public boolean saveUser(User user) {
        try {
            List<User> users = loadUsers();
            if (users.stream().anyMatch(u -> u.getId().equals(user.getId()))) {
                return false;
            }
            users.add(user);
            saveUsers(users);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving user: " + e.getMessage(), e);
            e.printStackTrace();
            return false;
        }
    }

    public User findUserById(String id) {
        List<User> users = loadUsers();
        return users.stream()
            .filter(user -> user.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    public boolean updateUser(User updatedUser) {
        try {
            List<User> users = loadUsers();
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId().equals(updatedUser.getId())) {
                    users.set(i, updatedUser);
                    saveUsers(users);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating user: " + e.getMessage(), e);
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String id) {
        try {
            List<User> users = loadUsers();
            boolean removed = users.removeIf(user -> user.getId().equals(id));
            if (removed) {
                saveUsers(users);
            }
            return removed;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting user: " + e.getMessage(), e);
            e.printStackTrace();
            return false;
        }
    }

    private List<User> loadUsers() {
        try (FileReader reader = new FileReader(userFile)) {
            List<User> users = gson.fromJson(reader, new TypeToken<List<User>>(){}.getType());
            return users != null ? users : new ArrayList<>();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading users: " + e.getMessage(), e);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveUsers(List<User> users) {
        try (FileWriter writer = new FileWriter(userFile)) {
            gson.toJson(users, writer);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving users: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }
}
