package kr.oyez.board.review.dto;

import lombok.Getter;

@Getter
public class ReviewResponseDto {

	private Long id;
	
	private String title;
	
	private String content;
	
	private String writerId;
	
	private String writer;
	
	private String subWriter;
	
	private String profileImg;
	
	private String filterId;
	
	private String filter;
	
	private String hashtag;
	
	private int rating;
	
	private int viewCnt;
	
	private int commentCnt;
	
	private int likesCnt;
	
	private String noticeYn;
	
	private String privateYn;
	
	private String useYn;
	
	private String regDate;
	
	private String updtDate;
}
