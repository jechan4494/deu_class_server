package controller;

import dao.ProfessorApprovedDAO;
import model.ProfessorApprovedModel;

public class ProfessorApprovedController {
    public static boolean cancelReservation(ProfessorApprovedModel reservation) {
        return ProfessorApprovedDAO.removeReservation(reservation);
    }
}
