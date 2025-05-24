package service;

import model.common.Reservation;
import model.user.User;
import java.util.List;

public interface ReservationService {
    // Create a new reservation
    boolean makeReservation(Reservation reservation);
    
    // Get all reservations for a specific user
    List<Reservation> getUserReservations(String name, String role);
    
    // Cancel a reservation
    boolean cancelReservation(Reservation reservation);
    
    // Get all reservations (for admin/TA view)
    List<Reservation> getAllReservations();
    
    // Update reservation state
    boolean updateReservationState(Reservation reservation, String newState);
    
    // Check if a room is available for the given time slots
    boolean isRoomAvailable(int roomNumber, String day, List<String> timeSlots, String roomType);
    
    boolean registerUser(User user);
    
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(String userId);
} 