package kr.oyez.board.review.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
public class ReviewRequestDto {

private Long id;
	
	private String boardSeq;
	
	private String title;
	
	private String content;
	
	private String writerId;
	
	private String titleImg;
	
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
	
	@QueryProjection
	public ReviewRequestDto(Long id, String boardSeq, String title, String content, String writerId, String titleImg,
			String filter, String hashtag, int rating, int viewCnt, int commentCnt, int likesCnt, String noticeYn,
			String privateYn, String useYn, String regDate) {
		this.id = id;
		this.boardSeq = boardSeq;
		this.title = title;
		this.content = content;
		this.writerId = writerId;
		this.titleImg = titleImg;
		this.filter = filter;
		this.hashtag = hashtag;
		this.rating = rating;
		this.viewCnt = viewCnt;
		this.commentCnt = commentCnt;
		this.likesCnt = likesCnt;
		this.noticeYn = noticeYn;
		this.privateYn = privateYn;
		this.useYn = useYn;
		this.regDate = regDate;
	}
}
