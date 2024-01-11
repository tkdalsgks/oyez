package kr.oyez.comment.domain;

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
public class Comment {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long boardId;
	
	private String content;
	
	private String memberId;
	
	private int rating;
	
	private String deleteYn;
	
	private String regDate;
	
	private String updtDate;
}
