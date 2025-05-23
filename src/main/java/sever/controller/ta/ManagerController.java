/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sever.controller.ta;
import org.json.JSONArray;
import org.json.JSONObject;
import server.model.ta.ReservationModel;

import java.util.List;

public class ManagerController {
    private ReservationModel model = new ReservationModel();

    public JSONArray getAllUsersAsJson() {
        List<String[]> users = model.loadUsers();
        JSONArray arr = new JSONArray();

        for (String[] row : users) {
            JSONObject obj = new JSONObject();
            obj.put("id", row[0]);
            obj.put("name", row[1]);
            obj.put("role", row[2]);
            obj.put("department", row[3]);
            arr.put(obj);
        }
        return arr;
    }

    public boolean deleteUser(String userId) {
        return model.deleteUser(userId);
    }
}