package kr.oyez.api.comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

import jakarta.servlet.http.HttpSession;
import kr.oyez.board.adapter.GsonLocalDateTimeAdapter;
import kr.oyez.comment.domain.Comment;
import kr.oyez.comment.dto.CommentRequestDto;
import kr.oyez.comment.dto.CommentResponseDto;
import kr.oyez.comment.service.CommentService;
import kr.oyez.common.utils.StringUtils;
import kr.oyez.member.dto.SessionMember;
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
	@Transactional
	@GetMapping("/comments/{boardId}")
	public JsonObject list_comment(@PathVariable("boardId") Long boardId, Model model) {
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
	 */
	@Transactional
	@PostMapping("/comments")
	public JsonObject save(@RequestBody CommentRequestDto params) {
		log.info("@@@ [COMMENT] save");
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		
		JsonObject jsonObj = new JsonObject();
		
		try {
			boolean save = saveComment(params, sessionMember);
			//commentService.updateCountComment(params);
			jsonObj.addProperty("result", save);
		} catch(DataAccessException e) {
			jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");
		} catch(Exception e) {
			jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
		}
		
		return jsonObj;
	}

	private boolean saveComment(CommentRequestDto params, SessionMember sessionMember) {
		CommentRequestDto saveComment = CommentRequestDto.builder()
				.boardId(params.getBoardId())
				.content(params.getContent().replaceAll("(\r\n|\r|\n|\n\r)", "<br>"))
				.memberId(sessionMember.getMemberId())
				.rating(params.getRating())
				.deleteYn("N")
				.adminDeleteYn("N")
				.regDate(StringUtils.dateTime())
				.build();
		
		boolean save = commentService.saveComment(saveComment);
		
		return save;
	}
	
	/**
	 * 댓글 수정
	 */
	@Transactional
	@PatchMapping("/comments/{id}")
	public JsonObject update_comment(@PathVariable(value = "id") Long id, @RequestBody CommentRequestDto params) {
		log.info("@@@ [COMMENT] modify {}", id);
		
		JsonObject jsonObj = new JsonObject();
		
		try {
			SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
			Optional<Comment> comment = commentService.findById(id);
			
			System.out.println("@@@@@@@@@@@@@@@ " + sessionMember.getMemberId());
			System.out.println("@@@@@@@@@@@@@@@ " + comment.get().getMemberId());
			
			if(sessionMember.getMemberId().equals(comment.get().getMemberId())) {
				CommentRequestDto updateComment = CommentRequestDto.builder()
						.id(id)
						.content(params.getContent().replaceAll("(\r\n|\r|\n|\n\r)", "<br>"))
						.rating(params.getRating())
						.updtDate(StringUtils.dateTime())
						.build();
			
				boolean update = commentService.updateComment(updateComment);
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
	public JsonObject delete_comment(@PathVariable("id") final Long id) {
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
