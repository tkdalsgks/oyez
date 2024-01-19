package kr.oyez.api.board;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import kr.oyez.board.community.domain.Board;
import kr.oyez.board.community.dto.CommunityRequestDto;
import kr.oyez.board.community.dto.CommunityResponseDto;
import kr.oyez.board.community.service.CommunityService;
import kr.oyez.board.review.dto.ReviewResponseDto;
import kr.oyez.board.review.service.ReviewService;
import kr.oyez.common.dto.MessageDto;
import kr.oyez.common.utils.StringUtils;
import kr.oyez.member.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardApiController {
	
	private final CommunityService communityService;
	private final ReviewService reviewService;
	
	private final HttpSession session;
	
	@Transactional
	@GetMapping("/community/trend")
	public Map<String, Object> commu_list_trend(@RequestParam(value = "size") String size, Pageable pageable, Model model) {
		
		Slice<CommunityResponseDto> commu = communityService.findByAllTrend(pageable);
		
		Map<String, Object> data = new HashMap<>();
		data.put("commu", commu);
		
		return data;
	}
	
	@Transactional
	@GetMapping("/community/recent")
	public Map<String, Object> commu_list_recent(@RequestParam(value = "size") String size, Pageable pageable, Model model) {
		
		Slice<CommunityResponseDto> commu = communityService.findByAllRecent(pageable);
		
		Map<String, Object> data = new HashMap<>();
		data.put("commu", commu);
		
		return data;
	}
	
	@Transactional
	@GetMapping("/review")
	public Map<String, Object> review_list(@RequestParam(value = "size") String size, Pageable pageable, Model model) {
		
		Slice<ReviewResponseDto> review = reviewService.findByAll(pageable);
		
		Map<String, Object> data = new HashMap<>();
		data.put("review", review);
		
		return data;
	}

	/**
	 * 자유게시판 - 신규 게시글 생성
	 */
	@Transactional
	@PostMapping("/community/save")
	public String community_save(@RequestBody Board params, Model model) {
		log.info("##### Board Page Save __ API #####");
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		
		saveCommu(params, sessionMember);
		//communityService.saveHashtag(params);
		
		MessageDto message = new MessageDto("게시글 생성이 완료되었습니다.", "/community/recent", RequestMethod.GET, null);
		
		return StringUtils.showMessageAndRedirect(message, model);
	}
	
	/**
	 * 리뷰게시판 - 신규 게시글 생성
	 */
	@Transactional
	@PostMapping("/review/save")
	public String review_save(@RequestBody Board params, Model model) {
		log.info("##### Board Page Save __ API #####");
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		
		saveReview(params, sessionMember);
		//communityService.saveHashtag(params);
		
		MessageDto message = new MessageDto("게시글 생성이 완료되었습니다.", "/review/recent", RequestMethod.GET, null);
		
		return StringUtils.showMessageAndRedirect(message, model);
	}
	
	/**
	 * 기존 게시글 수정
	 */
	@Transactional
	@PostMapping("/{boardId}/modify")
	public String update(@PathVariable(value = "boardId") Long id, final CommunityRequestDto params, Model model) {
		log.info("@@@ [FREE BOARD] Modify board_id {}", id);
		
		CommunityRequestDto updateBoard = CommunityRequestDto.builder()
				.title(params.getTitle())
				.content(params.getContent())
				.build();
		
		communityService.updateBoard(updateBoard);
		MessageDto message = new MessageDto("게시글 수정이 완료되었습니다.", "javascript:history.go(-2)", RequestMethod.POST, null);
		
		return StringUtils.showMessageAndRedirect(message, model);
	}
	
	/**
	 * 전체 - 게시글 삭제
	 
	@PostMapping("/{boardId}/delete")
	public String delete(@PathVariable(value = "boardId") Long id, final SearchDto queryParams, Model model) {
		log.info("@@@ [FREE BOARD] Delete board_id {}", id);
		
		freeBoardService.deleteBoard(id);
		MessageDto message = new MessageDto("게시글 삭제가 완료되었습니다.", "/free", RequestMethod.GET, StringUtils.queryParamsToMap(queryParams));
		
		return StringUtils.showMessageAndRedirect(message, model);
	}
	
	/**
	 * 전체 - 게시글 공개/비공개
	 
	@PostMapping("/{boardId}/private")
	public String publicOrPrivate(@PathVariable(value = "boardId") Long id, final SearchDto queryParams, Model model) {
		log.info("@@@ [FREE BOARD] Public OR Private board_id {}", id);
		
		FreeBoardResponseDto params = freeBoardService.findByBoardId(id);
		String privateYn = params.getPrivateYn();
		
		if("Y".equals(privateYn)) {
			freeBoardService.publicBoard(id);
		} else if("N".equals(privateYn)) {
			freeBoardService.privateBoard(id);
		}	
		
		MessageDto message = new MessageDto("게시글 전환이 완료되었습니다.", "/free", RequestMethod.POST, StringUtils.queryParamsToMap(queryParams));
		
		return StringUtils.showMessageAndRedirect(message, model);
	}
	*/
	
	private void saveCommu(Board params, SessionMember sessionMember) {
		Board saveCommu = Board.builder()
				.boardSeq("1")
				.title(params.getTitle())
				.content(params.getContent())
				.writerId(sessionMember.getMemberId())
				.titleImg(params.getTitleImg())
				.noticeYn(params.getNoticeYn())
				.privateYn("N")
				.filter(params.getFilter())
				.useYn("Y")
				.regDate(StringUtils.dateTime())
				.build();
		
		communityService.save(saveCommu);
	}

	private void saveReview(Board params, SessionMember sessionMember) {
		Board saveReview = Board.builder()
				.boardSeq("2")
				.title(params.getTitle())
				.content(params.getContent())
				.writerId(sessionMember.getMemberId())
				.titleImg(params.getTitleImg())
				.noticeYn(params.getNoticeYn())
				.privateYn("N")
				.filter(params.getFilter())
				.useYn("Y")
				.regDate(StringUtils.dateTime())
				.build();
		
		reviewService.save(saveReview);
	}
}
