package kr.oyez.member.repository.custom;

import org.springframework.stereotype.Repository;

import kr.oyez.member.domain.Member;
import kr.oyez.member.domain.MemberCertified;

@Repository
public interface MemberCertifiedRepositoryCustom {
	
	public MemberCertified selectCertifiedMember(String memberId);
	
	public void successCertifiedEmail(MemberCertified memberCertified);
	
	public void successCertifiedRole(Member member);
}
