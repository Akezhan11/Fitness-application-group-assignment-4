package service;

import repositories.AttendanceRepository;
import repositories.ClassBookingRepository;

public class StatisticsService {
    private final AttendanceRepository attendanceRepository;
    private final ClassBookingRepository bookingRepository;

    public StatisticsService(AttendanceRepository attendanceRepository,
                             ClassBookingRepository bookingRepository) {
        this.attendanceRepository = attendanceRepository;
        this.bookingRepository = bookingRepository;
    }

    public int totalVisits(int memberId) {
        return attendanceRepository.findByMemberId(memberId).size();
    }

    public int classBookingsCount(int classId) {
        return bookingRepository.countByFitnessClassId(classId);
    }
}
