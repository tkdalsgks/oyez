package kr.oyez.chat.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import kr.oyez.chat.domain.Room;
import kr.oyez.chat.dto.ChatRequestDto;
import kr.oyez.chat.dto.ChatResponseDto;
import kr.oyez.chat.dto.ChatRoomDto;
import kr.oyez.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

	private final ChatRepository chatRepository;
	
	private Map<String, ChatRoomDto> chatRoomDto;
	
	@PostConstruct
	private void init() {
		chatRoomDto = new LinkedHashMap<>();
	}
	
	public ChatRoomDto findByRoomId(String roomId) {
		return chatRepository.findByRoomId(roomId);
	}
	
	public List<Room> findAllRooms() {
		return chatRepository.findAllRooms();
	}
	
	public ChatRoomDto createChatRoom(String roomName) {
		ChatRoomDto room = ChatRoomDto.create(roomName);
		chatRoomDto.put(room.getRoomId(), room);
		
		return room;
	}
	
	public void insertChatRoom(ChatRoomDto createRoom) {
		chatRepository.insertChatRoom(createRoom);
	}
	
	public List<ChatResponseDto> findRoomMessage(String roomId) {
		return chatRepository.findRoomMessage(roomId);
	}
	
	public void insertChatMessage(ChatRequestDto chatMessage) {
		chatRepository.insertChatMessage(chatMessage);
	}

	public String findRoomName(String roomId) {
		return chatRepository.findRoomName(roomId);
	}
}
