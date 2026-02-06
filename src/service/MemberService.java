package service;

import entities.Member;
import exception.InvalidPhoneNumberException;
import exception.MemberNotFoundException;
import repositories.MemberRepository;

import java.util.ArrayList;
import java.util.List;


public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void addMember(Member member) {
        if (!isValidPhone(member.getPhoneNumber())) {
            throw new InvalidPhoneNumberException(member.getPhoneNumber());
        }
        memberRepository.save(member);
    }
    public void updateMember(Member member) {

        if (member.getId() <= 0) {
            throw new IllegalArgumentException("Invalid member id");
        }

        if (member.getEmail() == null || member.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        memberRepository.update(member);
    }

    public Member findMemberByid(int id) {
        Member m = memberRepository.findById(id);
        if (m == null) {
            throw new MemberNotFoundException(id);
        }
        return m;
    }
    public Member findMemberByEmail(String email) {
        Member m = memberRepository.findByEmail(email);
        if (m == null) {
            throw new MemberNotFoundException(email);
        }
        return m;
    }

    public Member findMemberByPhone(String phone){
        Member m = memberRepository.findByPhone(phone);
        if (m == null) {
            throw new MemberNotFoundException(phone);
        }
        return m;
    }
    public List<Member> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        if (members == null) {
            return new ArrayList<>(); // вместо null возвращаем пустой список
        }
        return members;
    }


    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\+?\\d{10,13}");
    }
    public List<Member> getFilteredMembers(Predicate<Member> filter) {
        return repository.findAll()
                .stream()
                .filter(filter)
                .toList();
    }
}
