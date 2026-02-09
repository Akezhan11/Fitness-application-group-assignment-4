package components.membership;

import repositories.AttendanceRepository;
import repositories.MembershipRepository;
import service.MembershipService;

public class MembershipComponent {
    private final MembershipService membershipService;

    public MembershipComponent(MembershipRepository membershipRepo,
                               AttendanceRepository attendanceRepo) {
        this.membershipService = new MembershipService(membershipRepo, attendanceRepo);
    }

    public MembershipService membershipService() {
        return membershipService;
    }
}
