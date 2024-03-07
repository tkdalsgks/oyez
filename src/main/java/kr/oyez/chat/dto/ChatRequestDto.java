package kr.oyez.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRequestDto {
	
	private String roomId;
	
	private String message;
	
	private String memberId;
	
	private String useYn;
	
	private String regUser;
	
	private String regDate;
}
