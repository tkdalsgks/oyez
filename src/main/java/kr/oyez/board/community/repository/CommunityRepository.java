package kr.oyez.board.community.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.oyez.board.community.domain.Board;
import kr.oyez.board.community.repository.custom.CommunityRepositoryCustom;

@Repository
public interface CommunityRepository extends JpaRepository<Board, Long>, CommunityRepositoryCustom {

	Optional<Board> findById(Long id);String findByBoardSeq(Long id);

	//public Optional<Board> findById(Long id);
	//public void saveBoard(FreeBoardRequestDto params);
	//public void updateBoard(FreeBoardRequestDto params);
	//public void deleteByBoardId(Long id);
	//public void countHits(Long id);
	//public void saveHashtag(FreeBoardRequestDto params);
	//public void publicBoard(Long id);
	//public void privateBoard(Long id);
}
