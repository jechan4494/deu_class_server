/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sever.controller.ta;
import org.json.*;
import shared.model.ta.Reservation;
import server.model.ta.ReservationModel;

import java.io.*;
import java.util.List;

public class ReservationController {
    private ReservationModel model;
    private LogController logController;

    public ReservationController() {
        this.model = new ReservationModel();
        this.logController = new LogController();
    }

    public void handleRequest(JSONObject request) {
        String type = request.getString("type");
        if (type.equals("approve")) {
            handleApprove(request);
        } else if (type.equals("reject")) {
            handleReject(request);
        }
    }

    public void handleApprove(JSONObject req) {
        Reservation approved = toReservation(req, "승인");
        saveToFile(approved, "approved_reservations.json");
        updateReservationState(approved, "승인");
        logController.saveTaLog("[대기→승인]", approved);
    }

    public void handleReject(JSONObject req) {
        Reservation rejected = toReservation(req, "거절");
        saveToFile(rejected, "rejected_reservations.json");
        updateReservationState(rejected, "거절");
        logController.saveTaLog("[대기→거절]", rejected);
    }

    private Reservation toReservation(JSONObject req, String state) {
        String name = req.getString("name");
        String role = req.getString("role");
        String roomType = req.getString("roomType");
        int roomNumber = req.getInt("roomNumber");
        String day = req.getString("day");

        JSONArray slotArray = req.getJSONArray("timeSlots");
        List<String> timeSlots = slotArray.toList().stream()
                .map(Object::toString)
                .toList();

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

