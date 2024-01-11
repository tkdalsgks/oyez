package kr.oyez.board.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import kr.oyez.board.community.dto.CommunityResponseDto;
import kr.oyez.board.community.service.CommunityService;
import kr.oyez.board.review.service.ReviewService;
import kr.oyez.comment.service.CommentService;
import kr.oyez.common.dto.MessageDto;
import kr.oyez.common.utils.StringUtils;
import kr.oyez.likes.service.LikesService;
import kr.oyez.member.domain.MemberCertified;
import kr.oyez.member.dto.SessionMember;
import kr.oyez.member.service.MemberService;
import kr.oyez.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommunityController {
	
	private final SecurityService securityService;
	private final MemberService memberService;
	private final CommunityService communityService;
	private final ReviewService reviewBoardService;
	private final CommentService commentService; 
	private final LikesService likesService;
	
	private final HttpSession session;
	
	/**
	 * 커뮤니티 리스트 - 트렌드
	 */
	@GetMapping({"/community", "/trend/week"})
	public String list_trend(Model model) {
		
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
			
			// 커뮤니티 게시글 갯수 - 최신
			Long board = communityService.countBoardRecent();
			model.addAttribute("commuCnt", board);
			
			return "board/community/list_trend";
		} else {
			return "index";
		}
	}
	
	/**
	 * 커뮤니티 리스트 - 트렌드
	 */
	@GetMapping("/recent")
	public String list_recent(Model model) {
		
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
			
			// 커뮤니티 게시글 갯수 - 트렌드
			Long board = communityService.countBoardTrend();
			model.addAttribute("commuCnt", board);
			
			return "board/community/list_recent";
		} else {
			return "index";
		}
	}
	
	
	/**
	 * 게시글 상세페이지 조회
	 */
	@Transactional
	@GetMapping("/posts/{id}")
	public String detail(@PathVariable(value = "id") Long id, 
						HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String referer = request.getHeader("referer");
		model.addAttribute("referer", referer);
		
		Cookie oldCookie = null;
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals("boardView")) {
					oldCookie = cookie;
				}
			}
		}
		
		if(oldCookie != null) {
			if(!oldCookie.getValue().contains("[" + id.toString() + "]")) {
				communityService.countHits(id);
				oldCookie.setValue(oldCookie.getValue() + "[" + id + "]");
				oldCookie.setPath("/");
				oldCookie.setMaxAge(60 * 60 * 24);
				response.addCookie(oldCookie);
			}
		} else {
			communityService.countHits(id);
			Cookie newCookie = new Cookie("boardView", "[" + id + "]");
			newCookie.setPath("/");
			newCookie.setMaxAge(60 * 60 * 24);
			response.addCookie(newCookie);
		}
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		if (sessionMember != null) {
			log.info("@@@ [COMMUNITY] Detail board_id {}", id);
			
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
			
			CommunityResponseDto board = communityService.findByBoard(id);
			System.out.println("@@@@@@@@@@@@@@@@@@@@ " + board);
			//Likes likes = new Likes();
			
			if(board != null) {	
				System.out.println("!!!!!!!!!!!!!!!!!!!! " + board);
				model.addAttribute("board", board);
				
				//likes.setBoardId(board.getId());
				//likes.setUserId(sessionMember.getMemberId());
				
				Long comment = commentService.countComment(id);
				//int likesAll = likesService.selectLikes(likes);
				
				if(comment != 0) {
					model.addAttribute("comment", comment);
				} else {
					model.addAttribute("comment", "0");
				}
				
				/*
				if(likesAll != 0) {
					model.addAttribute("likesAll", likesAll);
				} else {
					model.addAttribute("likesAll", likesAll);
				}
				
				if(likesService.findLikes(likes) == 0) {
					model.addAttribute("likes", 0);
				} else {
					model.addAttribute("likes", 1);
				}
				*/
			} else {
				MessageDto message = new MessageDto("존재하지 않거나 이미 삭제된 게시글입니다.", "/community", RequestMethod.POST, null);
				return StringUtils.showMessageAndRedirect(message, model);
			}
		}
		
		return "board/community/detail";
	}
	
	/*
	/**
	 * 게시글 작성 페이지
	 
	@GetMapping("/community/write")
	public String write(@PathVariable(value = "boardId") Long id, ReviewFilterDto filterDto, Model model) {
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		if (sessionMember != null) {
			log.info("@@@ [COMMUNITY] Write");
			
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
				FreeBoardResponseDto board = freeBoardService.findByBoardId(id);
				model.addAttribute("board", board);
			}
			
			// 게시글 필터
			List<ReviewFilterDto> filter = reviewBoardService.reviewFilter(filterDto);
			model.addAttribute("filter", filter);
			
			return "board/community/write";
		} else {
			return "index";
		}
	}
	
	/**
	 * 게시글 수정 페이지
	 
	@GetMapping("/{boardId}/modify")
	public String change(@PathVariable(value = "boardId") Long id, ReviewFilterDto filterDto, Model model) {
		log.info("@@@ [COMMUNITY] Modify board_id {}", id);
		
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
			
			FreeBoardResponseDto board = new FreeBoardResponseDto();
			
			if(id != null) {
				board = freeBoardService.findByBoardId(id);
				model.addAttribute("board", board);
			}
			
			// 게시글 필터
			List<ReviewFilterDto> filter = reviewBoardService.reviewFilter(filterDto);
			model.addAttribute("filter", filter);
			
			if("1".equals(board.getBoardSeq())) {
				return "board/community/modify";
			} else {
				return "board/review/modify";
			}
		} else {
			return "index";
		}
	}
	*/
}
