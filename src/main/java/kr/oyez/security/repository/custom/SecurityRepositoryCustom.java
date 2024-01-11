package kr.oyez.security.repository.custom;

import org.springframework.stereotype.Repository;

import kr.oyez.common.domain.LoginLog;
import kr.oyez.member.domain.MemberCertified;

@Repository
public interface SecurityRepositoryCustom {
	
	public void insertLoginLog(LoginLog loginLog);
	public MemberCertified selectCertifiedMember(String memberId);
}
