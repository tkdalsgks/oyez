package kr.oyez.api.chat;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import kr.oyez.chat.domain.Room;
import kr.oyez.chat.dto.ChatResponseDto;
import kr.oyez.chat.dto.ChatRoomDto;
import kr.oyez.chat.service.ChatService;
import kr.oyez.member.domain.MemberCertified;
import kr.oyez.member.dto.SessionMember;
import kr.oyez.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ChatApiController {
	
	private final MemberService memberService;
	private final ChatService roomService;
	
	private final HttpSession session;
	
	/**
	 * 채팅방 리스트 조회
	 */
	@Transactional
	@PostMapping("/chat/list")
	public List<Room> createRoom(Model model) {
		
		List<Room> roomList = null;
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		if (sessionMember != null) {
			log.info("@@@ [CHAT] Room List");
			
			model.addAttribute("memberId", sessionMember.getMemberId());
			model.addAttribute("memberName", sessionMember.getMemberNickname());
			model.addAttribute("memberEmail", sessionMember.getMemberEmail());
			model.addAttribute("role", sessionMember.getRole());
			
			// 계정 인증 여부
			MemberCertified certified = memberService.selectCertifiedMember(sessionMember.getMemberId());
			model.addAttribute("certified", "N");
			if(certified != null) {
				if("Y".equals(certified.getCertifiedYn())) {
					model.addAttribute("certified", "Y");
					
					// 채팅방 리스트
					roomList = roomService.findAllRooms();
					model.addAttribute("roomList", roomList);
				}
			}
		}
		
		return roomList;
	}
	
	/**
	 * 채팅방 개설
	 */
	@Transactional
	@PostMapping("/chat/room")
	public JsonObject create(@RequestParam(value = "name") String name, RedirectAttributes rttr, Model model) {
		log.info("##### ChatRoom Page Save __ API " + name + " #####");
		
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@     " + name);
		
		JsonObject jsonObj = new JsonObject();
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		
		ChatRoomDto room = roomService.createChatRoom(name);
		String roomId = room.getRoomId();
		String roomName = room.getName();
		String memberId = sessionMember.getMemberId();
		
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@     " + roomName);
		
		jsonObj.addProperty("roomId", roomId);
		jsonObj.addProperty("roomName", roomName);
		
		ChatRoomDto createRoom = ChatRoomDto.builder()
				.roomId(roomId)
				.name(roomName)
				.memberId(memberId)
				.build();
		
		log.info("##### ChatRoom INSERT __ API {}, {} #####", roomName, roomId);
		roomService.insertChatRoom(createRoom);
		
		return jsonObj;
	}
	
	@Transactional
	@PostMapping("/chat/message")
	public List<ChatResponseDto> chatRoomMessage(@RequestParam(value = "roomId") String roomId, Model model) {
		
		System.out.println("@@@@@@@@@@@@@@@@    " + roomId);
		
		List<ChatResponseDto> message = null;
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		if (sessionMember != null) {
			log.info("@@@ [CHAT] Room List");
			
			model.addAttribute("memberId", sessionMember.getMemberId());
			model.addAttribute("memberName", sessionMember.getMemberNickname());
			model.addAttribute("memberEmail", sessionMember.getMemberEmail());
			model.addAttribute("role", sessionMember.getRole());
			
			// 계정 인증 여부
			MemberCertified certified = memberService.selectCertifiedMember(sessionMember.getMemberId());
			model.addAttribute("certified", "N");
			if(certified != null) {
				if("Y".equals(certified.getCertifiedYn())) {
					model.addAttribute("certified", "Y");
					
					// 채팅방 리스트
					message = roomService.findRoomMessage(roomId);
					String roomName = roomService.findRoomName(roomId);
					model.addAttribute("message", message);
					model.addAttribute("roomName", roomName);
				}
			}
		}
		
		return message;
	}
}
