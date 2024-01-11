package kr.oyez.board.community.repository.custom;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import kr.oyez.board.community.dto.CommunityResponseDto;

@Repository
public interface CommunityRepositoryCustom {
	
	Slice<CommunityResponseDto> findByAllTrend(Pageable pageable);
	
	Slice<CommunityResponseDto> findByAllRecent(Pageable pageable);
	
	CommunityResponseDto findByBoard(Long id);
	
	Long countBoardTrend();
	
	Long countBoardRecent();
	
	Long countHits(Long id);
	
	//Page<CommunityResponseDto> findNotice(Pageable pageable);
	
	//Page<CommunityResponseDto> findLikes(Pageable pageable);
}
