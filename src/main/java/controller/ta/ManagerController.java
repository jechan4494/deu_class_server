/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.ta;
import server.model.ta.ReservationModel;
import java.util.*;

public class ManagerController {
    private final ReservationModel model = new ReservationModel();

    // 클라이언트에게 전달할 사용자 정보 (List<Map> 형식)
    public List<Object> getAllUsersAsList() {
        List<String[]> users = model.loadUsers();

        return users.stream().map(row -> {
            Map<String, String> user = new HashMap<>();
            user.put("id", row[0]);
            user.put("name", row[1]);
            user.put("role", row[2]);
            user.put("department", row[3]);
            return (Object) user;
        }).toList();
    }

    public boolean deleteUser(String userId) {
        return model.deleteUser(userId);
    }
}