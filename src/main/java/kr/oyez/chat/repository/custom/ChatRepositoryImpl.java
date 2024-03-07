package kr.oyez.chat.repository.custom;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import kr.oyez.chat.domain.QMessage;
import kr.oyez.chat.domain.QRoom;
import kr.oyez.chat.domain.Room;
import kr.oyez.chat.dto.ChatRequestDto;
import kr.oyez.chat.dto.ChatResponseDto;
import kr.oyez.chat.dto.ChatRoomDto;
import kr.oyez.common.utils.StringUtils;
import kr.oyez.member.domain.QMember;

public class ChatRepositoryImpl implements ChatRepositoryCustom {
	
	private final EntityManager em;
	private final JPAQueryFactory queryFactory;
	
	public ChatRepositoryImpl(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	QMember member = QMember.member;
	QRoom room = QRoom.room;
	QMessage message = QMessage.message1;
	
	@Override
	public ChatRoomDto findByRoomId(String roomId) {
		
		return null;
	}

	@Override
	public void insertChatRoom(ChatRoomDto params) {
		
		em.createNativeQuery("INSERT INTO CHAT_ROOM (ROOM_ID, NAME, PRIVATE_YN, USE_YN, REG_USER, REG_DATE) values (?, ?, ?, ?, ?, ?)")
			.setParameter(1, params.getRoomId())
			.setParameter(2, params.getName())
			.setParameter(3, "N")
			.setParameter(4, "Y")
			.setParameter(5, params.getMemberId())
			.setParameter(6, StringUtils.dateTime())
			.executeUpdate();
		
		em.close();
	}
	
	@Override
	public List<Room> findAllRooms() {
		
		List<Room> content = queryFactory.from(room)
				.select(room)
				.where(
						room.useYn.eq("Y")
				)
				.fetch();
		
		return content;
	}
	
	@Override
	public String findRoomName(String roomId) {
		
		return queryFactory.from(room)
				.select(room.name)
				.where(
						room.roomId.eq(roomId)
				)
				.fetchOne();
	}

	@Override
	public List<ChatResponseDto> findRoomMessage(String roomId) {
		
		List<ChatResponseDto> content = queryFactory.from(message)
				.select(Projections.constructor(ChatResponseDto.class,
						message.memberId,
						member.memberNickname,
						member.profileImg,
						message.message,
						message.regDate
						))
				.leftJoin(member).on(message.memberId.eq(member.memberId))
				.where(
						message.roomId.eq(roomId)
				)
				.orderBy(message.regDate.asc())
				.fetch();
		
		return content;
	}
	
	@Override
	public void insertChatMessage(ChatRequestDto params) {
		
		em.createNativeQuery("INSERT INTO CHAT_MESSAGE (ROOM_ID, MESSAGE, MEMBER_ID, USE_YN, REG_USER, REG_DATE) values (?, ?, ?, ?, ?, ?)")
			.setParameter(1, params.getRoomId())
			.setParameter(2, params.getMessage())
			.setParameter(3, params.getMemberId())
			.setParameter(4, "Y")
			.setParameter(5, params.getMemberId())
			.setParameter(6, StringUtils.dateTime())
			.executeUpdate();
		
		em.close();
	}
}
