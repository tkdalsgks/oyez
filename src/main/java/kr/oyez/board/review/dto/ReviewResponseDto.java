package kr.oyez.board.review.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {

	private long id;
	
	private String boardSeq;
	
	private String title;
	
	private String content;
	
	private String writerId;
	
	private String writer;
	
	private String titleImg;
	
	private String profileImg;
	
	private String filterCd;
	
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

	@QueryProjection
	public ReviewResponseDto(long id, String boardSeq, String title, String content, String writerId, String writer,
			String titleImg, String profileImg, String filterCd, String filter, int rating, int viewCnt, int commentCnt,
			int likesCnt, String noticeYn, String privateYn, String useYn, String regDate, String updtDate) {
		this.id = id;
		this.boardSeq = boardSeq;
		this.title = title;
		this.content = content;
		this.writerId = writerId;
		this.writer = writer;
		this.titleImg = titleImg;
		this.profileImg = profileImg;
		this.filterCd = filterCd;
		this.filter = filter;
		this.rating = rating;
		this.viewCnt = viewCnt;
		this.commentCnt = commentCnt;
		this.likesCnt = likesCnt;
		this.noticeYn = noticeYn;
		this.privateYn = privateYn;
		this.useYn = useYn;
		this.regDate = regDate;
		this.updtDate = updtDate;
	}
}
