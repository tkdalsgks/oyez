package kr.oyez.board.community.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import kr.oyez.board.community.domain.Board;
import kr.oyez.board.community.dto.CommunityResponseDto;
import kr.oyez.board.community.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunityService {

	private final CommunityRepository communityRepository;
	
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
	
	/*
	public Page<FreeBoardResponseDto> findNotice(FreeBoardCondition condition, Pageable pageable) {
		return freeBoardRepository.findNotice(condition, pageable);
	}
	
	public Page<FreeBoardResponseDto> findLikes(FreeBoardCondition condition, Pageable pageable) {
		return freeBoardRepository.findLikes(condition, pageable);
	}

	public void saveBoard(FreeBoardRequestDto params) {
		freeBoardRepository.saveBoard(params);
	}
	
	public void updateBoard(FreeBoardRequestDto params) {
		freeBoardRepository.updateBoard(params);
	}
	
	public void deleteBoard(Long id) {
		freeBoardRepository.deleteByBoardId(id);
	}
	
	public void countHits(Long id) {
		freeBoardRepository.countHits(id);
	}

	public void saveHashtag(FreeBoardRequestDto params) {
		//freeBoardRepository.saveHashtag(params);
	}

	public void publicBoard(Long id) {
		freeBoardRepository.publicBoard(id);
	}

	public void privateBoard(Long id) {
		freeBoardRepository.privateBoard(id);
	}
	*/
}
