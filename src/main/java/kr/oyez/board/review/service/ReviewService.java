package kr.oyez.board.review.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import kr.oyez.board.review.dto.ReviewRequestDto;
import kr.oyez.board.review.dto.ReviewResponseDto;
import kr.oyez.board.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	
	public Slice<ReviewResponseDto> findByAll(Pageable pageable) {
		
		return reviewRepository.findByAll(pageable);
	}

	public Long countBoard() {
		
		return reviewRepository.countBoard();
	}
	
	public ReviewResponseDto findByBoard(Long id) {
		
		return reviewRepository.findByBoard(id);
	}

	public void saveReview(ReviewRequestDto params) {
		
		reviewRepository.saveReview(params);
	}
	
	/*
	public List<ReviewBoardResponseDto> findNotice(SearchDto params) {
		return reviewRepository.findNotice(params);
	}
	
	public List<Likes> findLikesBestReview(SearchDto params) {
		return reviewRepository.findLikesBestReview(params);
	}
	
	public ReviewBoardResponseDto findByReviewId(Long id) {
		return reviewRepository.findByReviewId(id);
	}

	public void saveReview(ReviewBoardRequestDto params) {
		reviewRepository.saveReview(params);
	}
	
	public void updateReview(ReviewBoardRequestDto params) {
		reviewRepository.updateReview(params);
	}
	
	public void deleteReview(Long id) {
		reviewRepository.deleteByReviewId(id);
	}
	
	public void countHits(Long id) {
		reviewRepository.countHits(id);
	}

	public List<ReviewFilterDto> reviewFilter(ReviewFilterDto filterDto) {
		return reviewRepository.reviewFilter(filterDto);
	}

	public void saveHashtag(ReviewBoardRequestDto params) {
		reviewRepository.saveHashtag(params);
	}
	*/
}
