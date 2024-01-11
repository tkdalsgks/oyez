package kr.oyez.likes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.oyez.likes.entity.Likes;
import kr.oyez.likes.repository.custom.LikesRepositoryCustom;


@Repository
public interface LikesRepository extends JpaRepository<Likes, Long>, LikesRepositoryCustom {

	//public int selectLikes(Likes likes);
	//public int findLikes(Likes likes);
	//public int saveLikes(Likes likes);
	//public int deleteLikes(Likes likes);
	//public Object updateCountLikes(Likes likes);
	//public int selectTodayLikes(Likes likes);
}
