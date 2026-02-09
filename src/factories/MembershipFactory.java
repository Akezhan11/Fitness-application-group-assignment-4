package factories;

import entities.MembershipType;

import java.time.LocalDate;

public class MembershipFactory {

    private MembershipFactory() {}

    public static MembershipType create(MembershipKind kind, int memberId, Integer visitsLimit) {
        LocalDate start = LocalDate.now();

        return switch (kind) {
            case MONTHLY -> new MembershipType.Builder()
                    .memberId(memberId)
                    .type("MONTHLY")
                    .startDate(start)
                    .endDate(start.plusDays(30))
                    .active(true)
                    .build();
            case YEARLY -> new MembershipType.Builder()
                    .memberId(memberId)
                    .type("YEARLY")
                    .startDate(start)
                    .endDate(start.plusDays(365))
                    .active(true)
                    .build();
            case VISIT_BASED -> {
                int limit = (visitsLimit == null || visitsLimit <= 0) ? 10 : visitsLimit;

                yield new MembershipType.Builder()
                        .memberId(memberId)
                        .type("VISIT_" + limit)
                        .startDate(start)
                            .endDate(start.plusDays(180))
                        .active(true)
                        .build();
            }
        };
    }
}
