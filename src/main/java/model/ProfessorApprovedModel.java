package model;

import java.util.Objects;

public class ProfessorApprovedModel {
    private String professorName;
    private String professorId;
    private String roomNumber;
    private String day;
    private String time;
    private String state;

    // 기본 생성자
    public ProfessorApprovedModel() {}

    // 전체 필드 생성자
    public ProfessorApprovedModel(String professorName, String professorId, String roomNumber,
                                  String day, String time, String state) {
        this.professorName = professorName;
        this.professorId = professorId;
        this.roomNumber = roomNumber;
        this.day = day;
        this.time = time;
        this.state = state;
    }

    // ⬇ 필요한 getter/setter들
    public String getProfessorName() { return professorName; }
    public void setProfessorName(String professorName) { this.professorName = professorName; }

    public String getProfessorId() { return professorId; }
    public void setProfessorId(String professorId) { this.professorId = professorId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    // equals, hashCode도 override
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProfessorApprovedModel)) return false;
        ProfessorApprovedModel that = (ProfessorApprovedModel) o;
        return Objects.equals(professorName, that.professorName) &&
                Objects.equals(professorId, that.professorId) &&
                Objects.equals(roomNumber, that.roomNumber) &&
                Objects.equals(day, that.day) &&
                Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(professorName, professorId, roomNumber, day, time);
    }
}
