package kr.oyez.comment.repository.custom;

import java.util.List;

import kr.oyez.comment.dto.CommentRequestDto;
import kr.oyez.comment.dto.CommentResponseDto;

public interface CommentRepositoryCustom {

	public List<CommentResponseDto> findByAll(Long boardId);
	
	public Long countComment(Long boardId);
	
	public void saveComment(CommentRequestDto saveComment);
	
	public void updateComment(CommentRequestDto params);
	
	//public CommentResponseDTO findByCommentId(Long id);
	//public int saveComment(CommentRequestDTO params);
	///public int updateComment(CommentRequestDTO params);
	//public int deleteComment(Long id);
	//public int countComment(Long boardId);
	//public void updateCountComment(CommentRequestDTO params);
}
