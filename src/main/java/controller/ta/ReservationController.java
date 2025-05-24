/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.ta;
import shared.model.ta.Reservation;
import server.model.ta.ReservationModel;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.json.JSONObject;

public class ReservationController {
    private final ReservationModel model;
    private final LogController logController;

    public ReservationController() {
        this.model = new ReservationModel();
        this.logController = new LogController();
    }

    public void handleRequest(Map<String, Object> request) {
        String type = (String) request.get("type");
        if ("approve".equals(type)) {
            handleApprove(request);
        } else if ("reject".equals(type)) {
            handleReject(request);
        }
    }

    public void handleApprove(Map<String, Object> req) {
        Reservation approved = toReservation(req, "승인");
        saveToFile(approved, "approved_reservations.json");
        updateReservationState(approved, "승인");
        logController.saveTaLog("[대기→승인]", approved);
    }

    public void handleReject(Map<String, Object> req) {
        Reservation rejected = toReservation(req, "거절");
        saveToFile(rejected, "rejected_reservations.json");
        updateReservationState(rejected, "거절");
        logController.saveTaLog("[대기→거절]", rejected);
    }

    @SuppressWarnings("unchecked")
    private Reservation toReservation(Map<String, Object> req, String state) {
        String name = (String) req.get("name");
        String role = (String) req.get("role");
        String roomType = (String) req.get("roomType");
        int roomNumber = (int) req.get("roomNumber");
        String day = (String) req.get("day");
        List<String> timeSlots = (List<String>) req.get("timeSlots");

        return new Reservation(name, role, roomType, roomNumber, day, timeSlots, state);
    }

    private void saveToFile(Reservation reservation, String filename) {
        File file = new File(filename);
        JSONArray data = new JSONArray();

        try (FileReader reader = new FileReader(file)) {
            data = new JSONArray(new JSONTokener(reader));
        } catch (Exception e) {
            // 파일 없으면 새로 생성
        }

        JSONObject obj = new JSONObject();
        obj.put("name", reservation.getName());
        obj.put("role", reservation.getRole());
        obj.put("roomType", reservation.getType());
        obj.put("roomNumber", reservation.getRoomNumber());
        obj.put("day", reservation.getDay());
        obj.put("timeSlots", reservation.getTimeSlots());
        obj.put("state", reservation.getState());

        data.put(obj);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(data.toString(2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateReservationState(Reservation reservation, String newState) {
        File file = new File("reservations.json");

        try (FileReader reader = new FileReader(file)) {
            JSONArray arr = new JSONArray(new JSONTokener(reader));

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                if (
                    obj.getInt("roomNumber") == reservation.getRoomNumber() &&
                    obj.getString("day").equals(reservation.getDay()) &&
                    obj.getJSONArray("timeSlots").toString().equals(new JSONArray(reservation.getTimeSlots()).toString())
                ) {
                    obj.put("state", newState);
                    break;
                }
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(arr.toString(2));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


