package kr.oyez.chat.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.web.socket.WebSocketSession;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class ChatRoomDto {

	private Long id;
	
	private String roomId;
	
	private String name;
	
	private String privateYn;
	
	private String useYn;
	
	private String memberId;
	
	private String regDate;
	
	private Set<WebSocketSession> sessions = new HashSet<>();
	
	public static ChatRoomDto create(String name) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String now = sdf.format(date);
		
		ChatRoomDto room = new ChatRoomDto();
		UUID uuid = UUID.randomUUID();
		
		room.roomId = now + toUnsignedString(uuid.getMostSignificantBits(), 6) + toUnsignedString(uuid.getLeastSignificantBits(), 6);
		room.name = name;
		
		return room;
	}
	
	public static String toUnsignedString(long i, int shift) {
		
        char[] buf = new char[64];
        int charPos = 64;
        int radix = 1 << shift;
        long mask = radix - 1;
        long number = i;
        
        do {
            buf[--charPos] = digits[(int) (number & mask)];
            number >>>= shift;
        } while (number != 0);
        return new String(buf, charPos, (64 - charPos));
    }
	
    final static char[] digits = {
    		
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', '_', '*' // '.', '-'
    };
    
    @QueryProjection
	public ChatRoomDto(Long id, String roomId, String name, String privateYn, String useYn, String memberId,
			String regDate, Set<WebSocketSession> sessions) {
		this.id = id;
		this.roomId = roomId;
		this.name = name;
		this.privateYn = privateYn;
		this.useYn = useYn;
		this.memberId = memberId;
		this.regDate = regDate;
		this.sessions = sessions;
	}
}
