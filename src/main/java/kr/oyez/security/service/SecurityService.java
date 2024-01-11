package kr.oyez.security.service;

import org.springframework.stereotype.Service;

import kr.oyez.member.domain.Member;
import kr.oyez.member.domain.MemberCertified;
import kr.oyez.security.repository.SecurityRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityService {
	
	private final SecurityRepository securityRepository;
	
	public MemberCertified selectCertifiedMember(String memberId) {
		return securityRepository.selectCertifiedMember(memberId);
	}

	public Member findByMemberId(String memberId) {
		return securityRepository.findByMemberId(memberId);
	}

	public Member findByMemberNickname(String memberNickname) {
		return securityRepository.findByMemberNickname(memberNickname);
	}

	public Member findByMemberEmail(String memberEmail) {
		return securityRepository.findByMemberEmail(memberEmail);
	}
	
	
}
