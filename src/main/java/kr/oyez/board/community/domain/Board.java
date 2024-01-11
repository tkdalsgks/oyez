package kr.oyez.board.community.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Board {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String boardSeq;
	
	private String title;
	
	private String content;
	
	@Column(name = "member_id")
	private String writerId;
	
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
	
	public void board(long id, String title) {
		
	}
}
