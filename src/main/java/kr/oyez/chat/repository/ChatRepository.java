package kr.oyez.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.oyez.chat.domain.Room;
import kr.oyez.chat.repository.custom.ChatRepositoryCustom;

@Repository
public interface ChatRepository extends JpaRepository<Room, Long>, ChatRepositoryCustom {
	
	
}
