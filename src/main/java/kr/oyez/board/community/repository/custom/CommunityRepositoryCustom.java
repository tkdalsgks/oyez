package kr.oyez.board.community.repository.custom;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import kr.oyez.board.community.dto.CommunityRequestDto;
import kr.oyez.board.community.dto.CommunityResponseDto;
import kr.oyez.board.community.dto.NoticeResponseDto;

@Repository
public interface CommunityRepositoryCustom {
	
	List<NoticeResponseDto> findByNotice();
	
	Slice<CommunityResponseDto> findByAllTrend(Pageable pageable);
	
	Slice<CommunityResponseDto> findByAllRecent(Pageable pageable);
	
	void saveCommunity(CommunityRequestDto params);
	
	void saveHashtag(CommunityRequestDto params);
	
	String findByBoardSeq(Long id);
	
	CommunityResponseDto findByBoard(Long id);
	
	Long countBoardTrend();
	
	Long countBoardRecent();
	
	Long countHits(Long id);
	
	Long updateBoard(CommunityRequestDto params);
	
	void deleteBoard(CommunityRequestDto params);
	
	void publicBoard(Long id);
	
	void privateBoard(Long id);
	
	//Page<CommunityResponseDto> findNotice(Pageable pageable);
	
	//Page<CommunityResponseDto> findLikes(Pageable pageable);
}
