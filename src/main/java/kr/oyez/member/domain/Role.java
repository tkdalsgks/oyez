package kr.oyez.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

	SUPERADMIN("ROLE_SUPERADMIN", "최고 관리자"),
	
	ADMIN("ROLE_ADMIN", "관리자"),
	
	MEMBER("ROLE_MEMBER", "인증 회원"),
	
	EXPLORE("ROLE_EXPLORE", "둘러보기 회원"),
	
	UN_MEMBER("ROLE_UN_MEMBER", "탈퇴 및 추방 회원"),
	
	GUEST("ROLE_GUEST", "미인증 회원");
	
	private final String key;
	private final String title;
}
