package repositories;
import entities.Member;
import java.util.List;

public interface MemberRepository{
    void save(Member member);
    Member findById(int id);
    Member findByPhone(String phoneNumber);
    Member findByEmail(String email);
    List<Member> findAll();
    void update(Member member);
}
