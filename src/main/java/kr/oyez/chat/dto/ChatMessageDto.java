package kr.oyez.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

	private MessageType type;
	
	private String roomId;
	
	private String writer;
	
	private String writerId;
	
	private String message;
}
