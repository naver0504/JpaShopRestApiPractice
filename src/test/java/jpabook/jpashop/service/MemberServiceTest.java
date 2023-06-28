package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class MemberServiceTest {


    @Autowired
    private MemberService memberService;

    @Test
    void memberServiceTest() {
        Member member = new Member();
        member.setName("승진");

        memberService.join(member);
        Member findMember = memberService.findOne(member.getId());
        System.out.println("findMember = " + findMember.getName());


    }

    @Test
    void memberServiceTestV2() {
        Member member = new Member();
        member.setName("승진");

        Member member2 = new Member();
        member2.setName("백");

        memberService.join(member);
        memberService.join(member2);
        List<Member> members = memberService.findMembers();

        for (Member member1 : members) {
            System.out.println("member1 = " + member1.getName());
        }


    }

}