package kr.oyez.api.likes;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpSession;
import kr.oyez.likes.entity.Likes;
import kr.oyez.likes.service.LikesService;
import kr.oyez.member.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LikesApiController {

	private final LikesService likesService;
	
	private final HttpSession session;
	
	/**
	 * 게시글 좋아요, 취소
	 */
	@Transactional
	@PostMapping({"/community/likes", "/community/likes/{boardId}", "/review/likes", "/review/likes/{boardId}"})
	public JsonObject saveLikes(@PathVariable(value = "boardId", required = false) Long boardId, @RequestBody final Likes likes) {
		log.info("@@@ [LIKES] /api/v1/community/likes");
		log.info("@@@ [LIKES] /api/v1/review/likes");
		log.info("@@@ [LIKES] board_id {}", likes.getBoardId());
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		JsonObject jsonObj = new JsonObject();
		
		synchronized(this) {
			try {
				Long todayLikes = likesService.selectTodayLikes(sessionMember.getMemberId());
				if(todayLikes >= 5) {
					jsonObj.addProperty("todayLikes", todayLikes);
					jsonObj.addProperty("message", "좋아요는 하루 5개의 게시글만 가능합니다.");
				} else {
					boolean save = likesService.saveOrDeleteLikes(likes);
					jsonObj.addProperty("result", save);				
				}
			} catch(DataAccessException e) {
				jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");
			} catch(Exception e) {
				jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
			}
		}
		
		return jsonObj;
	}
}
