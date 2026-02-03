package entities;

import java.time.LocalDate;

public class MembershipType {

    private int id;
    private int memberId;
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    public MembershipType(){}
    public MembershipType(int memberId, String type, LocalDate startDate, LocalDate endDate, boolean active){
        setMemberId(memberId);
        setType(type);
        setStartDate(startDate);
        setEndDate(endDate);
        setActive(active);
    }
    public int getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getMemberId() {
        return memberId;
    }
    public void setMemberId(int memberId){
        this.memberId = memberId;
    }
    public String getType() {
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate){
        this.startDate = startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate){
        this.endDate = endDate;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active){
        this.active = active;
    }
    public boolean isExpired() {
        return endDate.isBefore(LocalDate.now());
    }
}
