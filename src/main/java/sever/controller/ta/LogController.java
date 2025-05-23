/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sever.controller.ta;
import shared.model.ta.Reservation;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.*;
import java.time.LocalDateTime;

public class LogController {
    private static final String LOG_PATH = "ta_log.json";

    // ✅ 로그 저장 (승인/거절)
    public void saveTaLog(String transition, Reservation reservation) {
        JSONObject log = new JSONObject();
        log.put("transition", transition);
        log.put("targetUser", reservation.getName());
        log.put("room", reservation.getType() + " " + reservation.getRoomNumber());
        log.put("time", reservation.getDay() + " " + reservation.getTimeSlots().get(0));
        log.put("timestamp", LocalDateTime.now().toString());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_PATH, true))) {
            writer.write(log.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ 단순 로그 저장 (예: 테스트, 시스템 이벤트용)
    public void saveLog(String role, String message) {
        JSONObject obj = new JSONObject();
        obj.put("timestamp", LocalDateTime.now().toString());
        obj.put("targetUser", role);
        obj.put("transition", message);
        obj.put("time", "시스템");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_PATH, true))) {
            writer.write(obj.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ 로그 전체 로딩 (클라이언트에게 전송용)
    public JSONArray loadLog() {
        JSONArray logArray = new JSONArray();
        File file = new File(LOG_PATH);

        if (!file.exists()) return logArray;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logArray.put(new JSONObject(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return logArray;
    }
}
