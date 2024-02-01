package kr.oyez.board.community.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeResponseDto {

	private long id;
	
	private String boardSeq;
	
	private String title;
	
	private String writerId;
	
	private String writer;
	
	private String noticeYn;
	
	private String useYn;
	
	private String regDate;
	
	private String updtDate;

	@QueryProjection
	public NoticeResponseDto(long id, String boardSeq, String title, String writerId, String writer, String noticeYn,
			String useYn, String regDate, String updtDate) {
		this.id = id;
		this.boardSeq = boardSeq;
		this.title = title;
		this.writerId = writerId;
		this.writer = writer;
		this.noticeYn = noticeYn;
		this.useYn = useYn;
		this.regDate = regDate;
		this.updtDate = updtDate;
	}
}
