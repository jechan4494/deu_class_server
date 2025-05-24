package network;

import controller.UserController;
import controller.ta.LogController;
import model.User;
import server.model.ta.ReservationModel;
import shared.model.ta.Reservation;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
            Socket client = this.socket;
            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(client.getInputStream())
        ) {
            ReservationModel model = new ReservationModel();
            LogController logController = new LogController();

            while (true) {
                Object cmdObj = ois.readObject();
                if (!(cmdObj instanceof String)) break;

                String command = (String) cmdObj;
                switch (command) {

                    case "register": {
                        User user = (User) ois.readObject();
                        boolean success = UserController.registerUser(user);
                        oos.writeObject(success ? "success" : "duplicate");
                        break;
                    }

                    case "login": {
                        String id = (String) ois.readObject();
                        String pw = (String) ois.readObject();
                        User user = UserController.login(id, pw);
                        oos.writeObject(user);
                        break;
                    }

                    case "loadApproved": {
                        List<Reservation> list = model.loadApprovedReservations();
                        oos.writeObject(list);
                        break;
                    }

                    case "loadRejected": {
                        List<Reservation> list = model.loadRejectedReservations();
                        oos.writeObject(list);
                        break;
                    }

                    case "loadReserved": {
                        List<Reservation> list = model.loadReservedReservations();
                        oos.writeObject(list);
                        break;
                    }

                    case "approve": {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> request = (Map<String, Object>) ois.readObject();
                        Reservation r = ReservationModel.fromJsonRequest(request, "승인");
                        model.saveApprovedReservation(r);
                        model.updateReservationState(r, "승인");
                        logController.saveTaLog("[대기→승인]", r);
                        oos.writeObject(Map.of("result", "success"));
                        break;
                    }

                    case "reject": {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> request = (Map<String, Object>) ois.readObject();
                        Reservation r = ReservationModel.fromJsonRequest(request, "거절");
                        model.saveRejectedReservation(r);
                        model.updateReservationState(r, "거절");
                        logController.saveTaLog("[대기→거절]", r);
                        oos.writeObject(Map.of("result", "success"));
                        break;
                    }

                    case "loadUsers": {
                        List<String[]> users = model.loadUsers();
                        oos.writeObject(users);
                        break;
                    }

                    case "deleteUser": {
                        String userId = (String) ois.readObject();
                        boolean deleted = model.deleteUser(userId);
                        oos.writeObject(deleted ? "success" : "fail");
                        break;
                    }

                    case "loadLog": {
                        List<Map<String, String>> logs = logController.loadLogAsList();
                        oos.writeObject(logs);
                        break;
                    }

                    case "logout": {
                        oos.writeObject("logged_out");
                        return; // 연결 종료
                    }

                    default:
                        oos.writeObject("unknown_command");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}