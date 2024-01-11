package kr.oyez.api.member;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.oyez.common.utils.StringUtils;
import kr.oyez.email.service.EmailService;
import kr.oyez.member.domain.Member;
import kr.oyez.member.dto.CreateMemberDto;
import kr.oyez.member.dto.SessionMember;
import kr.oyez.member.service.MemberService;
import kr.oyez.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberApiController {
	
	private final MemberService memberService;
	private final SecurityService securityService;
	private final EmailService emailService;
	
	/**
	 * 회원가입
	 */
	@PostMapping("/join")
	public String join(@RequestBody CreateMemberDto createMember) {
		
		memberService.joinMember(createMember);
		
		return "redirect:/";
	}
	
	/**
	 * 아이디 중복확인
	 */
	@ResponseBody
	@PostMapping("/check/id")
	public Map<String, String> checkId(@RequestParam("memberId") String memberId) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		if(securityService.findByMemberId(memberId) == null) {
			log.info("@@@ [CHECK] MEMBER_ID {}", memberId);
			
			map.put("result", "true");
		} else {
			log.info("@@@ [CHECK] Duplicate MEMBER_ID {}", memberId);
			
			map.put("result", "false");
		}
		
		return map;
	}
	
	/**
	 * 이메일 중복확인
	 */
	@ResponseBody
	@PostMapping("/check/email")
	public Map<String, String> checkEmail(@RequestParam("memberEmail") String memberEmail) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		if(securityService.findByMemberEmail(memberEmail) == null) {
			log.info("@@@ [CHECK] MEMBER_EMAIL {}", memberEmail);
			
			map.put("result", "true");
		} else {
			log.info("@@@ [CHECK] Duplicate MEMBER_EMAIL {}", memberEmail);
			
			map.put("result", "false");
			map.put("memberEmail", memberEmail);
		}
		
		return map;
	}
	
	/**
	 * 닉네임 중복확인
	 */
	@ResponseBody
	@PostMapping("/check/nickname")
	public Map<String, String> checkNickname(@RequestParam("memberNickname") String memberNickname) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		if(securityService.findByMemberNickname(memberNickname) == null) {
			log.info("@@@ [CHECK] MEMBER_NICKNAME {}", memberNickname);
			
			map.put("result", "true");
		} else {
			log.info("@@@ [CHECK] Duplicate MEMBER_NICKNAME {}", memberNickname);
			
			map.put("result", "false");
			map.put("memberNickname", memberNickname);
		}
		
		return map;
	}
	
	/**
	 * 프로필 업데이트
	 */
	@ResponseBody
	@PostMapping("/{memberId}/profile")
	public JsonObject saveProfile(@PathVariable(value = "memberId") String memberId, 
									@RequestBody Member member, 
									HttpSession session) {
		log.info("@@@ [PROFILE] UPDATE member_id {}", memberId);
		
		JsonObject jsonObj = new JsonObject();
		
		try {
			SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
			
			String formatDate = sessionMember.getProfileUpdtDate();
			LocalDateTime profileUpdtDate = StringUtils.formatDateTime(formatDate);
			
			LocalDateTime now = LocalDateTime.now();
			long day = ChronoUnit.DAYS.between(profileUpdtDate, now);
			if(day > 0) {
				boolean result = memberService.updateProfile(member);
				jsonObj.addProperty("result", result);
			} else {
				jsonObj.addProperty("result", "exceedCount");
				jsonObj.addProperty("message", "오늘의 프로필 변경 횟수를 이미 소모하였습니다.");
			}
		} catch(DataAccessException e) {
			jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");
		} catch(Exception e) {
			jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
		}
		return jsonObj;
	}
	
	/**
	 * 프로필 사진 업데이트
	 */
	@PostMapping("/upload/profileImg")
	public void profileImageUrlUpdate(HttpServletRequest request, HttpServletResponse response, MultipartHttpServletRequest upload) throws Exception {
		
		memberService.upload(request, response, upload);
	}
	
	/**
	 * 계정 권한 업데이트
	 */
	@ResponseBody
	@PostMapping("/{memberId}/account")
	public JsonObject certifiedAccount(@PathVariable(value = "memberId") String memberId,
										@RequestBody Member member) {
		log.info("@@@ [ACCOUNT] UPDATE member_id {}", memberId);
		
		JsonObject jsonObj = new JsonObject();
		
		try {
			emailService.certifiedEmail(member);
		} catch(DataAccessException e) {
			jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");
		} catch(Exception e) {
			jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
		}
		return jsonObj;
	}
}
