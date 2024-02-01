package kr.oyez.likes.repository.custom;

import kr.oyez.likes.entity.Likes;

public interface LikesRepositoryCustom {
	
	Long selectTodayLikes(String memberId);
	
	Long countBoardLikes(Likes likes);
	
	void saveLikes(Likes insertLikes);
	
	Long deleteLikes(Likes likes);
	
	void updateCountLikes(Likes likes);
}
