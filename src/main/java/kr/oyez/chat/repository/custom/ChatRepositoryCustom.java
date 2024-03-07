package kr.oyez.chat.repository.custom;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.oyez.chat.domain.Room;
import kr.oyez.chat.dto.ChatRequestDto;
import kr.oyez.chat.dto.ChatResponseDto;
import kr.oyez.chat.dto.ChatRoomDto;

@Repository
public interface ChatRepositoryCustom {
	
	ChatRoomDto findByRoomId(String roomId);
	
	List<Room> findAllRooms();
	
	String findRoomName(String roomId);
	
	void insertChatRoom(ChatRoomDto createRoom);
	
	List<ChatResponseDto> findRoomMessage(String roomId);
	
	void insertChatMessage(ChatRequestDto params);
}
