package kr.oyez.member.dto;

import java.io.Serializable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import kr.oyez.member.domain.Member;
import kr.oyez.member.domain.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class SessionMember implements Serializable {

	@Id @GeneratedValue
	private Long id;
	private String memberId;
	private String memberEmail;
	private String memberNickname;
	private String profileImg;
	private String regDate;
	private String provider;
	private String profileUpdtDate; 
	private Role role;
	
	public SessionMember(Member member) {
		this.id = member.getId();
		this.memberId = member.getMemberId();
		this.memberEmail = member.getMemberEmail();
		this.memberNickname = member.getMemberNickname();
		this.profileImg = member.getProfileImg();
		this.regDate = member.getRegDate();
		this.provider = member.getProvider();
		this.profileUpdtDate = member.getProfileUpdtDate();
		this.role = member.getRole();
	}
}
