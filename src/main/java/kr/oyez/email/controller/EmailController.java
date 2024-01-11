package kr.oyez.email.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import kr.oyez.common.dto.MessageDto;
import kr.oyez.common.utils.StringUtils;
import kr.oyez.email.service.EmailService;
import kr.oyez.member.domain.Member;
import kr.oyez.member.domain.MemberCertified;
import kr.oyez.member.domain.Role;
import kr.oyez.member.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class EmailController {
	
	private final EmailService emailService;
	
	private final HttpSession session;

	/**
	 * 계정 최초 1회 이메일 인증으로 권한 GUEST -> MEMBER
	 */
	@GetMapping("/certified")
	public String certifiedEmail(MemberCertified memberCertified, Member member, Model model) {
		log.info("@@@ [CERTIFIED] member_id {}", member.getMemberId());
		
		MessageDto message;
		
		SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
		
		MemberCertified certified = emailService.selectCertifiedMember(sessionMember.getMemberId());
		LocalDateTime regDate = StringUtils.formatDateTime(certified.getRegDate());
		LocalDateTime now = LocalDateTime.now();
		long minutes = ChronoUnit.MINUTES.between(regDate, now);
		
		if(certified.getCertifiedCode().equals(EmailService.ePwRand)) {
			if(minutes < 3) {
				// 현재 가지고 있는 Authentication 정보 호출
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				
				// 기존 정보 삭제 후 새로운 권한 추가
				Collection<GrantedAuthority> updatedAuthorities = new ArrayList<>(authentication.getAuthorities());
				updatedAuthorities.clear();
				updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
				
				// 새로운 권한을 Security에 저장
				Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), updatedAuthorities);
				SecurityContextHolder.getContext().setAuthentication(newAuth);
				
				// 세션에 저장된 권한을 GUEST -> USER 로 변경
				sessionMember.setRole(Role.MEMBER);
				
				// 계정 인증 및 권한 업데이트
				enableEmail(sessionMember);
				updateRole(sessionMember);
				
				message = new MessageDto("이메일 인증이 완료되었습니다. \n이제부터 OYEZ의 모든 콘텐츠를 즐기실 수 있습니다.", "/", RequestMethod.GET, null);
				
				return showMessageAndRedirect(message, model);
			} else {
				message = new MessageDto("인증코드 유효시간이 지났습니다. \n다시 인증해주세요.", "/", RequestMethod.GET, null);
				
				return showMessageAndRedirect(message, model);
			}
		} else {
			message = new MessageDto("인증코드가 일치하지 않습니다. \n링크가 올바른지 확인하거나 다시 인증해주세요.", "/", RequestMethod.GET, null);
			
			return showMessageAndRedirect(message, model);
		}
	}
	
	private void enableEmail(SessionMember sessionMember) {
		MemberCertified enableEmail = MemberCertified.builder()
				.memberId(sessionMember.getMemberId())
				.certifiedYn("Y")
				.UpdtDate(StringUtils.dateTime())
				.build();
		
		emailService.successCertifiedEmail(enableEmail);
	}
	
	private void updateRole(SessionMember sessionMember) {
		Member updateRole = Member.builder()
				.memberId(sessionMember.getMemberId())
				.role(sessionMember.getRole())
				.updtDate(StringUtils.dateTime())
				.build();
		
		emailService.successCertifiedRole(updateRole);
	}
	
	private String showMessageAndRedirect(final MessageDto message, Model model) {
		model.addAttribute("message", message);
		
		return "common/messageRedirect";
	}
}
