package components.statistics;

import repositories.AttendanceRepository;
import repositories.ClassBookingRepository;
import service.StatisticsService;

public class StatisticsComponent {
    private final StatisticsService statisticsService;

    public StatisticsComponent(AttendanceRepository attendanceRepo,
                               ClassBookingRepository bookingRepo) {
        this.statisticsService = new StatisticsService(attendanceRepo, bookingRepo);
    }

    public StatisticsService statisticsService() {
        return statisticsService;
    }
}
