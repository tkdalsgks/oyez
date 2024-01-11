package kr.oyez.member.dto;

import kr.oyez.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemberDto {
	
	private Long id;
	
	private String memberId;
	
	private String memberEmail;
	
	private String memberPwd;
	
	private String memberNickname;
	
	private String provider;
	
	private Role role;
	
	private String useYn;
	
	private String lockYn;
	
	private String joinDate;
	
	private String regUser;
	
	private String regDate;
	
	private String updtUser;
	
	private String updtDate;
	
	private String pwdUpdtDate;
	
	private String profileUpdtDate;
}
