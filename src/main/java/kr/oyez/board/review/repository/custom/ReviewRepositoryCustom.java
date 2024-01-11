package kr.oyez.board.review.repository.custom;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import kr.oyez.board.community.domain.Board;
import kr.oyez.board.community.dto.CommunityResponseDto;

@Repository
public interface ReviewRepositoryCustom {

	Slice<CommunityResponseDto> findByAll(Pageable pageable);
	
	Long countBoard();
	
	//Page<CommunityResponseDto> findNotice(Pageable pageable);
	
	//Page<CommunityResponseDto> findLikes(Pageable pageable);
}
