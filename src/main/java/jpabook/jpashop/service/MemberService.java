package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    /***
     * 회원 인증
     *
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 인증
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public Long deleteMember(String name) {
        Long id = memberRepository.deleteMemberByName(name);
        return id;

    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (findMembers.size() > 0) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).get();

    }

    @Transactional
    public void update(Long id, String name) {

        Optional<Member> member = memberRepository.findById(id);
        Member findMember = member.get();
        findMember.setName(name);


    }
}
