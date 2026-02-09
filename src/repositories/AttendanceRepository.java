package repositories;

import entities.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository {
    void save(Attendance attendance);
    boolean exists(int memberId, LocalDate date);
    List<Attendance> findByMemberId(int memberId);
    List<Attendance> findAll();

}
