package kr.oyez.api.comment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

import jakarta.servlet.http.HttpSession;
import kr.oyez.board.adapter.GsonLocalDateTimeAdapter;
import kr.oyez.comment.dto.CommentResponseDto;
import kr.oyez.comment.service.CommentService;
import kr.oyez.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentApiController {

	private final CommentService commentService;
	private final MemberService membeerService;
	
	private final HttpSession session;
	
	/**
	 * 댓글 리스트 조회
	 */
	@GetMapping("/comments/{boardId}")
	public JsonObject getCommentList(@PathVariable("boardId") Long boardId, Model model) {
		log.info("@@@ [COMMENT] Search List board_id {}", boardId);
		
		JsonObject jsonObj = new JsonObject();
		List<CommentResponseDto> commentList = commentService.findByAll(boardId);
		
		if(CollectionUtils.isEmpty(commentList) == false) {
			Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).create();
			JsonArray jsonArr = gson.toJsonTree(commentList).getAsJsonArray();
			jsonObj.add("commentList", jsonArr);
		}
		
		return jsonObj;
	}
	
	/**
	 * 댓글 작성
	 
	@PostMapping({"/comments", "/comments/{id}"})
	public JsonObject saveComment(@PathVariable(value = "id", required = false) Long id, @RequestBody final CommentRequestDTO params) {
		log.info("##### Comment Page Save __ API " + id + " #####");
		
		JsonObject jsonObj = new JsonObject();
		
		try {
			boolean save = commentService.saveComment(params);
			commentService.updateCountComment(params);
			jsonObj.addProperty("result", save);
		} catch(DataAccessException e) {
			jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");
		} catch(Exception e) {
			jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
		}
		
		return jsonObj;
	}
	
	/**
	 * 댓글 수정
	 
	@Transactional
	@PatchMapping({"/comments", "/comments/{id}"})
	public JsonObject updateComment(@PathVariable(value = "id", required = false) Long id, @RequestBody final CommentRequestDTO params) {
		log.info("##### Comment Page Modify __ API " + id + " #####");
		
		JsonObject jsonObj = new JsonObject();
		
		try {
			SessionUser sessionUser = (SessionUser) session.getAttribute("user");
			UserDTO user = userService.findByUserId(sessionUser.getUserEmail());
			CommentResponseDTO comment = commentService.findByCommentId(id);
			
			if(user.getUserId().equals(comment.getWriterId())) {
				boolean update = commentService.updateComment(params);
				jsonObj.addProperty("result", update);
				jsonObj.addProperty("message", "댓글이 수정되었습니다.");
			} else {
				jsonObj.addProperty("result", "isUnauthorized");
				jsonObj.addProperty("message", "권한이 없는 댓글입니다.");
			}
		} catch(DataAccessException e) {
			jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");
		} catch(Exception e) {
			jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
		}
		
		return jsonObj;
	}
	
	/**
	 * 댓글 삭제
	 
	@Transactional
	@DeleteMapping("/comments/{id}")
	public JsonObject deleteComment(@PathVariable("id") final Long id) {
		log.info("##### Comment Page Delete __ API " + id + " #####");
		
		JsonObject jsonObj = new JsonObject();
		
		try {
			SessionUser sessionUser = (SessionUser) session.getAttribute("user");
			UserDTO user = userService.findByUserId(sessionUser.getUserEmail());
			CommentResponseDTO comment = commentService.findByCommentId(id);
			
			if(user.getUserId().equals(comment.getWriterId())) {
				boolean delete = commentService.deleteComment(id);
				jsonObj.addProperty("result", delete);
				jsonObj.addProperty("message", "댓글이 삭제되었습니다.");
			} else {
				jsonObj.addProperty("result", "isUnauthorized");
				jsonObj.addProperty("message", "권한이 없는 댓글입니다.");
			}
		} catch(DataAccessException e) {
			jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");
		} catch(Exception e) {
			jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
		}
		
		return jsonObj;
	}*/
}
