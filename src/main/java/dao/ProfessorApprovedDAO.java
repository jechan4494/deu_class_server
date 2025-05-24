package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.ProfessorApprovedModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProfessorApprovedDAO {
    private static final String FILE_PATH = "approvedReservations.json";
    private static List<ProfessorApprovedModel> reservations = new ArrayList<>();

    static {
        loadReservations(); // 클래스 로딩 시 파일에서 불러오기
    }

    public static boolean removeReservation(ProfessorApprovedModel reservation) {
        Iterator<ProfessorApprovedModel> iter = reservations.iterator();
        while (iter.hasNext()) {
            ProfessorApprovedModel r = iter.next();
            if (r.equals(reservation) && ("대기".equals(r.getState()) || "승인".equals(r.getState()))) {
                r.setState("취소");
                saveReservations();  // ← 변경 후 저장
                return true;
            }
        }
        return false;
    }

    public static List<ProfessorApprovedModel> getReservations() {
        return reservations;
    }

    public static void saveReservations() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), reservations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadReservations() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                reservations = mapper.readValue(file, new TypeReference<List<ProfessorApprovedModel>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
