package services;

import entities.Membership;
import repositories.MembershipRepository;

import java.time.LocalDate;

public class MembershipService {

    private final MembershipRepository repository;

    public MembershipService(MembershipRepository repository) {
        this.repository = repository;
    }

    public Membership createMonthlyMembership(int memberId) {
        Membership membership = new Membership.Builder()
                .memberId(memberId)
                .type("MONTHLY")
                .price(15000)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .active(true)
                .build();

        repository.save(membership);
        return membership;
    }
}
