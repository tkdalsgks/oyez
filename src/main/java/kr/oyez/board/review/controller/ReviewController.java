package kr.oyez.board.review.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import kr.oyez.board.community.dto.CommunityResponseDto;
import kr.oyez.board.review.dto.ReviewResponseDto;
import kr.oyez.board.review.service.ReviewService;
import kr.oyez.common.domain.Common;
import kr.oyez.common.service.CommonService;
import kr.oyez.member.domain.MemberCertified;
import kr.oyez.member.dto.SessionMember;
import kr.oyez.member.service.MemberService;
import kr.oyez.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReviewController {
	
	private final CommonService commonService;
	private final SecurityService securityService;
	private final MemberService memberService;
	private final ReviewService reviewService;
	
	private final HttpSession session;
	
	/**
	 * 게시글 리스트 조회
	 */
	@GetMapping("/review")
	public String list(Model model) {
		log.info("@@@ [REVIEW] Search List");
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		if (sessionMember != null) {
			log.info("@@@ [MAIN]");
			
			model.addAttribute("memberId", sessionMember.getMemberId());
			model.addAttribute("memberName", sessionMember.getMemberNickname());
			model.addAttribute("memberEmail", sessionMember.getMemberEmail());
			model.addAttribute("role", sessionMember.getRole());
			
			// 계정 인증 여부
			MemberCertified certified = memberService.selectCertifiedMember(sessionMember.getMemberId());
			model.addAttribute("certified", "N");
			if(certified != null) {
				if("Y".equals(certified.getCertifiedYn())) {
					model.addAttribute("certified", "Y");
				}
			}
			
			// 프로필 사진
			if(sessionMember.getProfileImg() == null) {
				model.addAttribute("profileImg", null);
			} else {
				model.addAttribute("profileImg", sessionMember.getProfileImg());
			}
			
			// 리뷰 게시글 갯수
			Long board = reviewService.countBoard();
			model.addAttribute("reviewCnt", board);
			
			System.out.println("@@@@@@@@@@@@@@@@@ " + board);
			
			return "board/review/list";
		} else {
			return "index";
		}
	}
	
	/**
	 * 게시글 작성 페이지
	 */
	@GetMapping("/review/write")
	public String write(@RequestParam(value = "boardId", required = false) Long id, Model model) {
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		if (sessionMember != null) {
			log.info("@@@ [COMMUNITY] Search List");
			
			model.addAttribute("memberId", sessionMember.getMemberId());
			model.addAttribute("memberName", sessionMember.getMemberNickname());
			model.addAttribute("memberEmail", sessionMember.getMemberEmail());
			model.addAttribute("role", sessionMember.getRole());
			
			// 계정 인증 여부
			MemberCertified certified = memberService.selectCertifiedMember(sessionMember.getMemberId());
			model.addAttribute("certified", "N");
			if(certified != null) {
				if("Y".equals(certified.getCertifiedYn())) {
					model.addAttribute("certified", "Y");
				}
			}
			
			// 프로필 사진
			if(sessionMember.getProfileImg() == null) {
				model.addAttribute("profileImg", null);
			} else {
				model.addAttribute("profileImg", sessionMember.getProfileImg());
			}
			
			if(id != null) {
				ReviewResponseDto board = reviewService.findByBoard(id);
				model.addAttribute("board", board);
			}
			
			// 게시글 필터
			List<Common> filter = commonService.reviewFilter();
			model.addAttribute("filter", filter);
			
			return "board/review/write";
		} else {
			return "index";
		}
	}
}
