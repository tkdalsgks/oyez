package kr.oyez.comment.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRequestDto {

	private Long id;
	
	private String boardId;
	
	private String content;
	
	private String writerNo;
	
	private String writer;
	
	private String writerId;
	
	private int rating;
}
