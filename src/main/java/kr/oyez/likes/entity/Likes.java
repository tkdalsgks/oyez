package kr.oyez.likes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BOARD_LIKES")
public class Likes {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long boardId;
	
	private String memberId;
	
	private String useYn;
	
	private String regDate;
}
