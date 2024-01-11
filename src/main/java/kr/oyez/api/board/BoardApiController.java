package kr.oyez.api.board;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.oyez.board.community.dto.CommunityResponseDto;
import kr.oyez.board.community.service.CommunityService;
import kr.oyez.board.review.service.ReviewService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardApiController {
	
	private final CommunityService communityService;
	private final ReviewService reviewService;
	
	private final HttpSession session;
	
	@GetMapping("/community/trend")
	public Map<String, Object> commu_list_trend(@RequestParam(value = "size") String size, Pageable pageable, Model model) {
		
		Slice<CommunityResponseDto> commu = communityService.findByAllTrend(pageable);
		
		Map<String, Object> data = new HashMap<>();
		data.put("commu", commu);
		
		return data;
	}
	
	@GetMapping("/community/recent")
	public Map<String, Object> commu_list_recent(@RequestParam(value = "size") String size, Pageable pageable, Model model) {
		
		Slice<CommunityResponseDto> commu = communityService.findByAllRecent(pageable);
		
		Map<String, Object> data = new HashMap<>();
		data.put("commu", commu);
		
		return data;
	}
	
	@GetMapping("/review")
	public Map<String, Object> review_list(@RequestParam(value = "size") String size, Pageable pageable, Model model) {
		
		Slice<CommunityResponseDto> review = reviewService.findByAll(pageable);
		
		Map<String, Object> data = new HashMap<>();
		data.put("review", review);
		
		return data;
	}

	/**
	 * 자유게시판 - 신규 게시글 생성
	 
	@PostMapping("/free/save")
	public String save(final FreeBoardRequestDto params, Model model) {
		log.info("##### Board Page Save __ API #####");
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		params.setWriterId(sessionMember.getMemberId());
		
		//params.setContent(params.getContent().replaceAll("(\r\n|\r|\n|\n\r)", "<br>"));
		freeBoardService.saveBoard(params);
		freeBoardService.saveHashtag(params);
		
		MessageDto message = new MessageDto("게시글 생성이 완료되었습니다.", "/free", RequestMethod.POST, null);
		
		return StringUtils.showMessageAndRedirect(message, model);
	}
	
	/**
	 * 자유게시판 - 기존 게시글 수정
	 
	@PostMapping("/{boardId}/modify")
	public String update(@PathVariable(value = "boardId") Long id, final FreeBoardRequestDto params, Model model) {
		log.info("@@@ [FREE BOARD] Modify board_id {}", id);
		
		freeBoardService.updateBoard(params);
		MessageDto message = new MessageDto("게시글 수정이 완료되었습니다.", "javascript:history.go(-2)", RequestMethod.POST, null);
		
		return StringUtils.showMessageAndRedirect(message, model);
	}
	
	/**
	 * 리뷰게시판 - 신규 게시글 생성
	 
	@PostMapping("/review/save")
	public String save(final ReviewBoardRequestDto params, Model model) {
		log.info("##### Board Page Save __ API #####");
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		params.setWriterId(sessionMember.getMemberId());
		//params.setContent(params.getContent().replaceAll("(\r\n|\r|\n|\n\r)", "<br>"));
		reviewBoardService.saveReview(params);
		reviewBoardService.saveHashtag(params);
		
		MessageDto message = new MessageDto("게시글 생성이 완료되었습니다.", "/review", RequestMethod.POST, null);
		
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
}
