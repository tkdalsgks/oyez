package kr.oyez.api.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.transaction.Transactional;
import kr.oyez.chat.dto.ChatMessageDto;
import kr.oyez.chat.dto.ChatRequestDto;
import kr.oyez.chat.service.ChatService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StompChatController {
	
	private final ChatService roomService;
	
	private final SimpMessagingTemplate smt;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Transactional
	@MessageMapping("/chat/enter")
	public void enter(ChatMessageDto message) {
		message.setMessage("[" + message.getWriter() + "]님이 채팅방에 참여하였습니다.");
		smt.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
		
		ChatRequestDto chatMessage = new ChatRequestDto();
		chatMessage.setRoomId(message.getRoomId());
		chatMessage.setMessage("[" + message.getWriter() + "]님이 채팅방에 참여하였습니다.");
		chatMessage.setMemberId("ADMIN");
		roomService.insertChatMessage(chatMessage);
	}
	
	@Transactional
	@MessageMapping("/chat/leave")
	public void leave(ChatMessageDto message) {
		message.setMessage("[" + message.getWriter() + "]님이 채팅방에서 퇴장하였습니다.");
		smt.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
		
		ChatRequestDto chatMessage = new ChatRequestDto();
		chatMessage.setRoomId(message.getRoomId());
		chatMessage.setMessage("[" + message.getWriter() + "]님이 채팅방에서 퇴장하였습니다.");
		chatMessage.setMemberId("ADMIN");
		roomService.insertChatMessage(chatMessage);
	}
	
	@Transactional
	@MessageMapping("/chat/message")
	public void message(ChatMessageDto message) {
		log.info("##### WRITER : {}, " + " CHAT : {} #####", message.getWriter(), message.getMessage());
		
		message.setMessage(message.getMessage());
		
		ChatRequestDto chatMessage = new ChatRequestDto();
		chatMessage.setRoomId(message.getRoomId());
		chatMessage.setMessage(message.getMessage());
		chatMessage.setMemberId(message.getWriterId());
		roomService.insertChatMessage(chatMessage);
		
		smt.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
	}
}
