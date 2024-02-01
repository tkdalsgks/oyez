package kr.oyez.comment.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
public class CommentResponseDto {
	
	private Long id;
	
	private Long boardId;
	
	private String content;
	
	private String memberId;
	
	private String memberNickname;
	
	private String profileImg;
	
	private int rating;
	
	private String deleteYn;
	
	private String adminDeleteYn;
	
	private String regDate;
	
	private String updtDate;
	
	@QueryProjection
	public CommentResponseDto(Long id, Long boardId, String content, String memberId, String memberNickname,
			String profileImg, int rating, String deleteYn, String adminDeleteYn, String regDate, String updtDate) {
		this.id = id;
		this.boardId = boardId;
		this.content = content;
		this.memberId = memberId;
		this.memberNickname = memberNickname;
		this.profileImg = profileImg;
		this.rating = rating;
		this.deleteYn = deleteYn;
		this.adminDeleteYn = adminDeleteYn;
		this.regDate = regDate;
		this.updtDate = updtDate;
	}
}
