package kr.oyez.member.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import kr.oyez.board.community.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_no")
	private Long id;
	
	private String memberId;
	
	private String memberEmail;
	
	private String memberPwd;
	
	private String memberNickname;
	
	private String profileImg;
	
	private String provider;
	
	private String lockYn;
	
	private String lockDate;
	
	private String useYn;
	
	private int failCnt;
	
	private String joinDate;
	
	private String regUser;
	
	private String regDate;
	
	private String updtUser;
	
	private String updtDate;
	
	private String pwdUpdtDate;
	
	private String profileUpdtDate;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	public void createMember(String memberId, String memberPwd) {
		this.memberId = memberId;
		this.memberPwd = memberPwd;
	}
	
	public Member nickname(String memberNickname) {
		return Member.builder()
				.memberNickname(memberNickname)
				.build();
	}
	
	public String getRoleKey() {
		return this.getRole().getKey();
	}
}
