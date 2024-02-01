package kr.oyez.member.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import kr.oyez.member.domain.MemberCertified;
import kr.oyez.member.dto.SessionMember;
import kr.oyez.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping({"", "/"})
	public String index(HttpSession session, Model model) {
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		if (sessionMember != null) {
			log.info("@@@ [MAIN]");
			
			log.info("@@@ " + bCryptPasswordEncoder.encode("test"));
			
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
			
			return "main";
		} else {
			return "index";
		}
	}
	
	@GetMapping("/join")
	public String join_form(Model model) {
		
		log.info("@@@ [JOIN]");
		
		return "member/join";
	}
	
	@GetMapping("/{memberId}/profile")
	public String profile(@PathVariable(value = "memberId") String memberId, HttpSession session, Model model) {
		log.info("@@@ [PROFILE] member_id {}", memberId);
		
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
			
			return "member/profile";
		} else {
			return "index";
		}
	}
	
	@GetMapping("/{memberId}/account")
	public String account(@PathVariable(value = "memberId", required = false) String memberId, HttpSession session, Model model) {
		log.info("@@@ [ACCOUNT] member_id {}", memberId);
		
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
			
			return "member/account";
		} else {
			return "index";
		}
	}
}
