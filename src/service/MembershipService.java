package service;

import entities.MembershipType;
import exception.MembershipExpiredException;
import repositories.MembershipRepository;

import java.time.LocalDate;

public class MembershipService {
    private final MembershipRepository membershipRepository;
    public MembershipService(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }
    public void buyMembership(int memberId, String type, int price, int days) {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(days);

        MembershipType m = new MembershipType.Builder()
                .memberId(memberId)
                .type(type)
                .price(price)
                .startDate(start)
                .endDate(end)
                .active(true)
                .build();
        membershipRepository.save(m);
    }

    public void checkActive(int memberId) {
        MembershipType m = membershipRepository.findByMemberId(memberId);
        if (m == null || !m.isActive()) {
            throw new MembershipExpiredException();
        }
    }

    public void update(int memberId, String newType, int days) {

        MembershipType old = membershipRepository.findByMemberId(memberId);
        if (old == null) {
            throw new RuntimeException("Membership not found");
        }
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(days);

        MembershipType updated = new MembershipType.Builder()
                .id(old.getId())
                .memberId(old.getMemberId())
                .type(newType)
                .price(old.getPrice())
                .startDate(start)
                .endDate(end)
                .active(true)
                .build();

        membershipRepository.update(updated);
    }
    public MembershipType findByMemberId(int memberId) {
        MembershipType membership = membershipRepository.findByMemberId(memberId);
        if (membership == null) {
            System.out.println("No membership found for member ID " + memberId);
        }
        return membership;
    }
    public void deactivate(int memberId) {
        MembershipType membership = membershipRepository.findByMemberId(memberId);
        if (membership == null) {
            throw new RuntimeException("Membership not found");
        }
        membershipRepository.deactivate(memberId);
    }
}
