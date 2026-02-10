package repositories;
import entities.Member;
import java.util.List;

public interface MemberRepository extends Repository<Member> {
    void save(Member member);
    Member findByPhone(String phoneNumber);
    Member findByEmail(String email);
    void update(Member member);
}
