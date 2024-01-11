package kr.oyez.likes.repository.custom;

import kr.oyez.likes.entity.Likes;

public interface LikesRepositoryCustom {
	
	Long selectTodayLikes(String memberId);
	
	Long countBoardLikes(Likes likes);
	
	Long deleteLikes(Likes likes);
	
	Long updateCountLikes(Likes likes);
}
