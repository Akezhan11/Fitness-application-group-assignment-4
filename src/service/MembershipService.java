package service;

import entities.MembershipType;
import exception.MembershipExpiredException;
import factories.MembershipFactory;
import factories.MembershipKind;
import repositories.AttendanceRepository;
import repositories.MembershipRepository;

import java.time.LocalDate;

public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final AttendanceRepository attendanceRepository;

    public MembershipService(MembershipRepository membershipRepository,
                             AttendanceRepository attendanceRepository) {
        this.membershipRepository = membershipRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public void buyMembership(int memberId, MembershipKind kind, Integer visitsLimit) {
        MembershipType m = MembershipFactory.create(kind, memberId, visitsLimit);
        membershipRepository.save(m);
    }

    public void checkActive(int memberId) {
        MembershipType m = membershipRepository.findByMemberId(memberId);

        if (m == null || !m.isActive()) {
            throw new MembershipExpiredException();
        }
        if (m.isExpired()) {
            throw new MembershipExpiredException();
        }
        if (isVisitBased(m.getType())) {
            int limit = parseVisitLimit(m.getType());
            int visitsUsed = countVisitsInPeriod(memberId, m.getStartDate(), m.getEndDate());

            if (visitsUsed >= limit) {
                throw new MembershipExpiredException();
            }
        }
    }

    private boolean isVisitBased(String type) {
        return type != null && type.toUpperCase().startsWith("VISIT_");
    }

    private int parseVisitLimit(String type) {
        try {
            String[] parts = type.split("_");
            return Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return 10;
        }
    }
    private int countVisitsInPeriod(int memberId, LocalDate start, LocalDate end) {
        return (int) attendanceRepository.findByMemberId(memberId)
                .stream()
                .map(a -> a.getVisitDate())
                .filter(d -> (d.isEqual(start) || d.isAfter(start)) &&
                        (d.isEqual(end) || d.isBefore(end)))
                .count();
    }
}
