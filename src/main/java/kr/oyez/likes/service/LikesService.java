package kr.oyez.likes.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.oyez.common.utils.StringUtils;
import kr.oyez.likes.entity.Likes;
import kr.oyez.likes.repository.LikesRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikesService {
	
	private final LikesRepository likesRepository;
	
	public Long selectTodayLikes(String memberId) {
		
		return likesRepository.selectTodayLikes(memberId);
	}
	
	@Transactional
	public boolean saveOrDeleteLikes(Likes likes) {
		Long queryResult = 0L;
		
		if (likes.getBoardId() != null) {
			Long likesCnt = likesRepository.countBoardLikes(likes);
			
			if(likesCnt != 0) {
				queryResult = likesRepository.deleteLikes(likes);
				likesRepository.updateCountLikes(likes);
			} else {
				queryResult = insertLikes(likes);
				likesRepository.updateCountLikes(likes);
			}
		}
		
		return (queryResult >= 1) ? true : false;
	}

	private Long insertLikes(Likes likes) {
		Long queryResult;
		
		Likes insertLikes = Likes.builder()
				.boardId(likes.getBoardId())
				.memberId(likes.getMemberId())
				.useYn("Y")
				.regDate(StringUtils.date())
				.build();
		
		likesRepository.saveLikes(insertLikes);
		queryResult = 1L;
		
		return queryResult;
	}
}
