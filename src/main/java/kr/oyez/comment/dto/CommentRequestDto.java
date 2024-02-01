package kr.oyez.comment.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
public class CommentRequestDto {

	private Long id;
	
	private String boardId;
	
	private String content;
	
	private String memberId;
	
	private int rating;
	
	private String deleteYn;
	
	private String adminDeleteYn;
	
	private String regDate;
	
	private String updtDate;
	
	@QueryProjection
	public CommentRequestDto(Long id, String boardId, String content, String memberId, int rating, String deleteYn,
			String adminDeleteYn, String regDate, String updtDate) {
		this.id = id;
		this.boardId = boardId;
		this.content = content;
		this.memberId = memberId;
		this.rating = rating;
		this.deleteYn = deleteYn;
		this.adminDeleteYn = adminDeleteYn;
		this.regDate = regDate;
		this.updtDate = updtDate;
	}
}
