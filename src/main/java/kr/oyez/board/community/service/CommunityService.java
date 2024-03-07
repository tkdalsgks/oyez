package kr.oyez.board.community.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import kr.oyez.board.community.domain.Board;
import kr.oyez.board.community.dto.CommunityRequestDto;
import kr.oyez.board.community.dto.CommunityResponseDto;
import kr.oyez.board.community.dto.NoticeResponseDto;
import kr.oyez.board.community.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunityService {

	private final CommunityRepository communityRepository;
	
	public List<NoticeResponseDto> findByNotice() {
		
		return communityRepository.findByNotice();
	}
	
	/**
	 * 커뮤니티 리스트 - 트렌드
	 */
	public Slice<CommunityResponseDto> findByAllTrend(Pageable pageable) {
		
		return communityRepository.findByAllTrend(pageable);
	}
	
	/**
	 * 커뮤니티 리스트 - 최신
	 */
	public Slice<CommunityResponseDto> findByAllRecent(Pageable pageable) {
		
		return communityRepository.findByAllRecent(pageable);
	}
	
	public Optional<Board> findById(Long id) {
		
		return communityRepository.findById(id);
	}

	public CommunityResponseDto findByBoard(Long id) {
		
		return communityRepository.findByBoard(id);
	}

	public Long countBoardTrend() {
		
		return communityRepository.countBoardTrend();
	}
	
	public Long countBoardRecent() {
		
		return communityRepository.countBoardRecent();
	}

	public Long countHits(Long id) {
		
		return communityRepository.countHits(id);
	}

	public void saveCommunity(CommunityRequestDto params) {
		
		communityRepository.saveCommunity(params);
	}
	
	public String findByBoardSeq(Long id) {
		
		return communityRepository.findByBoardSeq(id);
	}

	public void modifyBoard(CommunityRequestDto params) {
		
		communityRepository.modifyBoard(params);
	}

	public void deleteBoard(CommunityRequestDto params) {
		
		communityRepository.deleteBoard(params);
	}

	public void publicBoard(Long id) {
		
		communityRepository.publicBoard(id);
	}

	public void privateBoard(Long id) {
		
		communityRepository.privateBoard(id);
	}
}
