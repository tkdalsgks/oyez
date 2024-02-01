package kr.oyez.comment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.oyez.comment.domain.Comment;
import kr.oyez.comment.dto.CommentRequestDto;
import kr.oyez.comment.dto.CommentResponseDto;
import kr.oyez.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	
	public List<CommentResponseDto> findByAll(final Long boardId) {
		return commentRepository.findByAll(boardId);
	}

	public Long countComment(final Long boardId) {
		return commentRepository.countComment(boardId);
	}
	
	public boolean saveComment(CommentRequestDto params) {
		
		int queryResult = 0;

		if (params.getId() == null) {
			commentRepository.saveComment(params);
			queryResult = 1;
		}
		
		return (queryResult == 1) ? true : false;
	}

	public boolean updateComment(CommentRequestDto params) {
		
		int queryResult = 0;

		if (params.getId() != null) {
			commentRepository.updateComment(params);
			queryResult = 1;
		}
		
		return (queryResult == 1) ? true : false;
	}

	public Optional<Comment> findById(Long id) {
		
		return commentRepository.findById(id);
	}
	
	/*
	public CommentResponseDto findByCommentId(final Long id) {
		return commentMapper.findByCommentId(id);
	}
	
	@Transactional
	public boolean updateComment(CommentRequestDto params) {
		int queryResult = 0;

		if (params.getId() != null) {
			queryResult = commentMapper.updateComment(params);
		}

		return (queryResult == 1) ? true : false;
	}
	
	@Transactional
	public boolean deleteComment(Long id) {
		int queryResult = 0;
		
		CommentResponseDto comment = commentMapper.findByCommentId(id);
		
		if (comment != null && "N".equals(comment.getDeleteYn())) {
			queryResult = commentMapper.deleteComment(id);
		}

		return (queryResult == 1) ? true : false;
	}
	
	public void updateCountComment(CommentRequestDto params) {
		commentMapper.updateCountComment(params);
	}
	*/
}
