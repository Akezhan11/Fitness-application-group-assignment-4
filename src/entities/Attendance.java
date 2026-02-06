package entities;

import java.time.LocalDate;

public class Attendance {

    private int id;
    private int memberId;
    private LocalDate visitDate;

    public Attendance() {}

    public Attendance(int memberId, LocalDate visitDate) {
        this.memberId = memberId;
        this.visitDate = visitDate;
    }

    public Attendance(int id, int memberId, LocalDate visitDate) {
        this.id = id;
        this.memberId = memberId;
        this.visitDate = visitDate;
    }

    public int getId() {
        return id;
    }

    public int getMemberId() {
        return memberId;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }
}
