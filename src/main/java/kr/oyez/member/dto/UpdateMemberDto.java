package kr.oyez.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberDto {

	private String memberId;
	
	private String memberNickname;
	
	private String memberEmail;
	
	private String profileUpdtDate;
	
	private String updtUser;
}
