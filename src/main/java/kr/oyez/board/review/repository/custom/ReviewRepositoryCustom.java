package kr.oyez.board.review.repository.custom;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import kr.oyez.board.review.dto.ReviewRequestDto;
import kr.oyez.board.review.dto.ReviewResponseDto;

@Repository
public interface ReviewRepositoryCustom {

	Slice<ReviewResponseDto> findByAll(Pageable pageable);
	
	void saveReview(ReviewRequestDto params);
	
	void saveHashtag(ReviewRequestDto params);
	
	Long countBoard();
	
	ReviewResponseDto findByBoard(Long id);
	
	//Page<CommunityResponseDto> findNotice(Pageable pageable);
	
	//Page<CommunityResponseDto> findLikes(Pageable pageable);
}
