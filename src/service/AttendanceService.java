package service;

import entities.Attendance;
import repositories.AttendanceRepository;

import java.time.LocalDate;
import java.util.List;

public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public void markVisit(int memberId, LocalDate date) {
        if (attendanceRepository.exists(memberId, date)) {
            throw new IllegalArgumentException("Attendance already exists for this member on this date");
        }
        attendanceRepository.save(new Attendance(memberId, date));
    }

    public List<Attendance> history(int memberId) {
        return attendanceRepository.findByMemberId(memberId);
    }
}
