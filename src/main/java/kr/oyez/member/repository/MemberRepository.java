package kr.oyez.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.oyez.member.domain.Member;
import kr.oyez.member.repository.custom.MemberRepositoryCustom;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

	public Member findByMemberId(String memberEmail);
	
	public Member findByMemberPwd(String memberId);
}
