package kr.oyez.api.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import kr.oyez.board.community.dto.NoticeResponseDto;
import kr.oyez.board.community.service.CommunityService;
import kr.oyez.board.review.dto.ReviewRequestDto;
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
	
	@GetMapping("/notice")
	public Map<String, Object> notice(Model model) {
		
		List<NoticeResponseDto> notice = communityService.findByNotice();
		
		Map<String, Object> data = new HashMap<>();
		data.put("notice", notice);
		
		return data;
	}
	
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
	public String community_save(@RequestBody CommunityRequestDto params, Model model) {
		log.info("@@@ [POST] Community New Post");
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		
		saveCommu(params, sessionMember);
		saveCommuHashtag(params);
		
		MessageDto message = new MessageDto("게시글 생성이 완료되었습니다.", "/community/recent", RequestMethod.GET, null);
		
		return StringUtils.showMessageAndRedirect(message, model);
	}
	
	/**
	 * 리뷰게시판 - 신규 게시글 생성
	 */
	@Transactional
	@PostMapping("/review/save")
	public String review_save(@RequestBody ReviewRequestDto params, Model model) {
		log.info("@@@ [POST] Review New Post boardId");
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		
		saveReview(params, sessionMember);
		saveReviewHashtag(params);
		
		MessageDto message = new MessageDto("게시글 생성이 완료되었습니다.", "/review/recent", RequestMethod.GET, null);
		
		return StringUtils.showMessageAndRedirect(message, model);
	}
	
	/**
	 * 기존 게시글 수정
	 */
	@Transactional
	@PostMapping("/{boardId}/modify")
	public String update(@PathVariable(value = "boardId") Long id, final CommunityRequestDto params, Model model) {
		log.info("@@@ [POST] Post Modify board_id {}", id);
		
		updateBoard(params);
		
		MessageDto message = new MessageDto("게시글 수정이 완료되었습니다.", "javascript:history.go(-2)", RequestMethod.POST, null);
		
		return StringUtils.showMessageAndRedirect(message, model);
	}
	
	/**
	 * 전체 - 게시글 삭제
	 */
	@Transactional
	@PostMapping("/{boardId}/delete")
	public String delete(@PathVariable(value = "boardId") Long id, final CommunityRequestDto params, Model model) {
		log.info("@@@ [POST] Delete Post boardId {}" + id);
		
		CommunityRequestDto deleteBoard = CommunityRequestDto.builder()
				.id(id)
				.useYn("N")
				.build();
				
		communityService.deleteBoard(deleteBoard);
		
		MessageDto message = new MessageDto("게시글 삭제가 완료되었습니다.", "javascript:history.go(-1)", RequestMethod.GET, null);
		
		return StringUtils.showMessageAndRedirect(message, model);
	}
	
	/**
	 * 전체 - 게시글 공개/비공개
	 */
	@Transactional
	@PostMapping("/{boardId}/private")
	public String publicOrPrivate(@PathVariable(value = "boardId") Long id, Model model) {
		log.info("@@@ [POST] Public Or Private Post boardId {}" + id);
		
		Optional<Board> params = communityService.findById(id);
		String privateYn = params.get().getPrivateYn();
		
		if("Y".equals(privateYn)) {
			communityService.publicBoard(id);
		} else if("N".equals(privateYn)) {
			communityService.privateBoard(id);
		}	
		
		MessageDto message = new MessageDto("게시글 전환이 완료되었습니다.", "/free", RequestMethod.POST, null);
		
		return StringUtils.showMessageAndRedirect(message, model);
	}
	
	private void saveCommu(CommunityRequestDto params, SessionMember sessionMember) {
		CommunityRequestDto saveCommu = CommunityRequestDto.builder()
				.boardSeq("1")
				.title(params.getTitle())
				.content(params.getContent())
				.writerId(sessionMember.getMemberId())
				.titleImg(params.getTitleImg())
				.noticeYn(params.getNoticeYn())
				.privateYn(params.getPrivateYn())
				.filter(params.getFilter())
				.useYn("Y")
				.regDate(StringUtils.dateTime())
				.build();
		
		communityService.saveCommunity(saveCommu);
	}

	private void saveReview(ReviewRequestDto params, SessionMember sessionMember) {
		ReviewRequestDto saveReview = ReviewRequestDto.builder()
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
		
		reviewService.saveReview(saveReview);
	}
	
	private void saveCommuHashtag(CommunityRequestDto params) {
		CommunityRequestDto saveHashtag = CommunityRequestDto.builder()
				.id(params.getId())
				.hashtag(params.getHashtag())
				.useYn("Y")
				.regDate(StringUtils.dateTime())
				.build();
		
		communityService.saveHashtag(saveHashtag);
	}
	
	private void saveReviewHashtag(ReviewRequestDto params) {
		CommunityRequestDto saveHashtag = CommunityRequestDto.builder()
				.id(params.getId())
				.hashtag(params.getHashtag())
				.useYn("Y")
				.regDate(StringUtils.dateTime())
				.build();
		
		communityService.saveHashtag(saveHashtag);
	}
	
	private void updateBoard(final CommunityRequestDto params) {
		CommunityRequestDto updateBoard = CommunityRequestDto.builder()
				.title(params.getTitle())
				.content(params.getContent())
				.build();
		
		communityService.updateBoard(updateBoard);
	}
}
