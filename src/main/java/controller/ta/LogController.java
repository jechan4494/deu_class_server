/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.ta;
import shared.model.ta.Reservation;
import org.json.JSONObject;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

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

    // ✅ 클라이언트 전송용: 로그를 List<Map> 형식으로 반환
    public List<Map<String, String>> loadLogAsList() {
        List<Map<String, String>> logs = new ArrayList<>();
        File file = new File(LOG_PATH);

        if (!file.exists()) return logs;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JSONObject obj = new JSONObject(line);
                Map<String, String> log = new HashMap<>();
                log.put("timestamp", obj.optString("timestamp"));
                log.put("transition", obj.optString("transition"));
                log.put("targetUser", obj.optString("targetUser"));
                log.put("room", obj.optString("room"));
                log.put("time", obj.optString("time"));
                logs.add(log);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return logs;
    }
}
