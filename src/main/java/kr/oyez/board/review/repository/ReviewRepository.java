package kr.oyez.board.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.oyez.board.community.domain.Board;
import kr.oyez.board.review.repository.custom.ReviewRepositoryCustom;

@Repository
public interface ReviewRepository extends JpaRepository<Board, Long>, ReviewRepositoryCustom {

	//public List<ReviewBoardResponseDto> findAll(SearchDto params);
	//public List<ReviewBoardResponseDto> findNotice(SearchDto params);
	//public List<Likes> findLikesBestReview(SearchDto params);
	//public ReviewBoardResponseDto findByReviewId(Long id);
	//public void saveReview(ReviewBoardRequestDto params);
	//public void updateReview(ReviewBoardRequestDto params);
	//public void deleteByReviewId(Long id);
	//public int countReview(SearchDto params);
	//public void countHits(Long id);
	//public List<ReviewFilterDto> reviewFilter(ReviewFilterDto filterDto);
	//public void saveHashtag(ReviewBoardRequestDto params);
}
