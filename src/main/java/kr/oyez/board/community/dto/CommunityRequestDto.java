package kr.oyez.board.community.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommunityRequestDto {

	private Long id;
	
	private String title;
	
	private String content;
	
	private String writerId;
	
	private String writer;
	
	private Boolean noticeYn;
	
	private String privateYn;
	
	private String filter;
	
	private String filterId;
	
	private String boardId;
	
	private String hashtag;
}
