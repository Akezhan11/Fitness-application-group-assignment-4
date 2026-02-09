package service;

import utils.Filter;
import utils.ListUtils;

import entities.Member;
import exception.InvalidPhoneNumberException;
import exception.MemberNotFoundException;
import repositories.MemberRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public Member findMemberByPhone(String phone) {
        Member m = memberRepository.findByPhone(phone);
        if (m == null) {
            throw new MemberNotFoundException(phone);
        }
        return m;
    }

    public List<Member> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        if (members == null) {
            return new ArrayList<>();
        }
        return members;
    }

    public List<Member> getFilteredMembers(Filter<Member> filter) {
        return ListUtils.filter(getAllMembers(), filter);
    }

    public List<Member> getSortedMembers(Comparator<Member> comparator) {
        List<Member> members = getAllMembers();
        return members.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
    public List<Member> getMembersByGender(String gender) {
        return getFilteredMembers(m ->
                m.getGender() != null && m.getGender().equalsIgnoreCase(gender));
    }

    public List<Member> getMembersByEmailDomain(String domain) {
        return getFilteredMembers(m ->
                m.getEmail() != null && m.getEmail().toLowerCase().endsWith(domain.toLowerCase()));
    }

    public List<Member> getMembersWithNameStartsWith(String prefix) {
        return getFilteredMembers(m ->
                m.getName() != null && m.getName().startsWith(prefix));
    }
    public List<Member> sortBySurnameThenName() {
        return getSortedMembers(
                Comparator.comparing(Member::getSurname, Comparator.nullsLast(String::compareToIgnoreCase))
                        .thenComparing(Member::getName, Comparator.nullsLast(String::compareToIgnoreCase))
        );
    }
    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\+?\\d{10,13}");
    }

}
