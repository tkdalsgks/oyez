package kr.oyez.board.community.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class CommunityRequestDto {

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
	public CommunityRequestDto(Long id, Long boardId, String title, String content, String writerId, String writer,
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
