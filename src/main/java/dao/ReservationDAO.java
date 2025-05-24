package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.ProfessorApprovedModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReservationDAO {
    private static final String RESERVATION_JSON = "reservations.json";
    private static List<ProfessorApprovedModel> reservationList = new ArrayList<>();
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        loadFromJson();
    }

    // JSON에서 예약 리스트 로드
    private static void loadFromJson() {
        try {
            File file = new File(RESERVATION_JSON);
            if (file.exists()) {
                reservationList = mapper.readValue(file, new TypeReference<List<ProfessorApprovedModel>>() {});
            } else {
                reservationList = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            reservationList = new ArrayList<>();
        }
    }

    // 예약 취소 메서드
    public static boolean cancelReservation(ProfessorApprovedModel target) {
        Iterator<ProfessorApprovedModel> iter = reservationList.iterator();
        while (iter.hasNext()) {
            ProfessorApprovedModel r = iter.next();
            if (r.equals(target)) { // equals가 정확히 비교하도록 구현 필요
                iter.remove();
                saveToJson();
                return true;
            }
        }
        return false;
    }

    // 예약 리스트 JSON 파일에 저장
    private static void saveToJson() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(RESERVATION_JSON), reservationList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
