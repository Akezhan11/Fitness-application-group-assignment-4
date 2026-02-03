package repositories;

import entities.MembershipType;

public interface MembershipRepository {

    void save(MembershipType membership);

    MembershipType findByMemberId(int memberId);

    void update(MembershipType membership);

    void deactivate(int memberId);
}
