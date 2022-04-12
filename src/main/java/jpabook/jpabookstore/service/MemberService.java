package jpabook.jpabookstore.service;

import jpabook.jpabookstore.domain.Member;
import jpabook.jpabookstore.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    /*
     회원가입
     */
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    /*
     전체회원 조회
     */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    /*
     회원 id로 조회
     */
    public Member findOne(Long memberId){
        return memberRepository.find(memberId);
    }
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
                throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }

}