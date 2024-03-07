package kr.oyez.chat.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatResponseDto {
	
	private String memberId;
	
	private String memberNickname;
	
	private String profileImg;
	
	private String message;
	
	private String regDate;

	@QueryProjection
	public ChatResponseDto(String memberId, String memberNickname, String profileImg, String message, String regDate) {
		this.memberId = memberId;
		this.memberNickname = memberNickname;
		this.profileImg = profileImg;
		this.message = message;
		this.regDate = regDate;
	}
}
