package kr.oyez.board.community.dto;

import lombok.Data;

@Data
public class CommunityCondition {

	private long id;
	
	private String boardSeq;
	
	private String title;
	
	private String content;
	
	private String writerId;
	
	private String writer;
	
	private String filter;
	
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
