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
import org.springframework.web.bind.annotation.ResponseBody;
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
	public void community_save(@RequestBody CommunityRequestDto params, Model model) {
		log.info("@@@ [POST] Community New Post");
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		
		saveCommu(params, sessionMember);
	}
	
	/**
	 * 리뷰게시판 - 신규 게시글 생성
	 */
	@Transactional
	@PostMapping("/review/save")
	public void review_save(@RequestBody ReviewRequestDto params, Model model) {
		log.info("@@@ [POST] Review New Post boardId");
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		
		saveReview(params, sessionMember);
	}
	
	/**
	 * 기존 게시글 수정
	 */
	@Transactional
	@PostMapping("/{boardId}/modify")
	public void update(@PathVariable(value = "boardId") Long id, @RequestBody CommunityRequestDto params, Model model) {
		log.info("@@@ [POST] Post Modify board_id {}", id);
		
		System.out.println("@@@@@@@@@@@@@@@@@@ " + params.getPrivateYn());
		
		modifyBoard(id, params);
	}
	
	/**
	 * 전체 - 게시글 삭제
	 */
	@Transactional
	@PostMapping("/{boardId}/delete")
	public void delete(@PathVariable(value = "boardId") Long id, Model model) {
		log.info("@@@ [POST] Delete Post boardId {}" + id);
		
		deleteBoard(id);
	}
	
	/**
	 * 전체 - 게시글 공개/비공개
	 */
	@Transactional
	@PostMapping("/{boardId}/private")
	public void publicOrPrivate(@PathVariable(value = "boardId") Long id, Model model) {
		log.info("@@@ [POST] Public Or Private Post boardId {}" + id);
		
		Optional<Board> params = communityService.findById(id);
		String privateYn = params.get().getPrivateYn();
		
		if("Y".equals(privateYn)) {
			communityService.publicBoard(id);
		} else if("N".equals(privateYn)) {
			communityService.privateBoard(id);
		}
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
				.hashtag(params.getHashtag())
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
				.hashtag(params.getHashtag())
				.useYn("Y")
				.regDate(StringUtils.dateTime())
				.build();
		
		reviewService.saveReview(saveReview);
	}
	
	private void modifyBoard(Long id, final CommunityRequestDto params) {
		CommunityRequestDto modifyBoard = CommunityRequestDto.builder()
				.id(id)
				.title(params.getTitle())
				.content(params.getContent())
				.noticeYn(params.getNoticeYn())
				.privateYn(params.getPrivateYn())
				.build();
		
		communityService.modifyBoard(modifyBoard);
	}
	
	private void deleteBoard(Long id) {
		CommunityRequestDto deleteBoard = CommunityRequestDto.builder()
				.id(id)
				.useYn("N")
				.updtDate(StringUtils.dateTime())
				.build();
				
		communityService.deleteBoard(deleteBoard);
	}
}
