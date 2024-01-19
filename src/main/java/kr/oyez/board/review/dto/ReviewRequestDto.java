package kr.oyez.board.review.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewRequestDto {

	private Long id;
	
	private Long boardId;
	
	private String title;
	
	private String content;
	
	private String writerId;
	
	private String writer;
	
	private String titleImg;
	
	private String noticeYn;
	
	private String privateYn;
	
	private String filterCd;
	
	private String filter;
	
	private String hashtag;

	@QueryProjection
	public ReviewRequestDto(Long id, Long boardId, String title, String content, String writerId, String writer,
			String titleImg, String noticeYn, String privateYn, String filterCd, String filter, String hashtag) {
		this.id = id;
		this.boardId = boardId;
		this.title = title;
		this.content = content;
		this.writerId = writerId;
		this.writer = writer;
		this.titleImg = titleImg;
		this.noticeYn = noticeYn;
		this.privateYn = privateYn;
		this.filterCd = filterCd;
		this.filter = filter;
		this.hashtag = hashtag;
	}
}
