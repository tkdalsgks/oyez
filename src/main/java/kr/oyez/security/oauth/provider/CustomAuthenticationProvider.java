package kr.oyez.security.oauth.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;
import kr.oyez.common.utils.StringUtils;
import kr.oyez.member.domain.Member;
import kr.oyez.member.dto.LockMemberDto;
import kr.oyez.member.dto.SessionMember;
import kr.oyez.member.service.MemberService;
import kr.oyez.security.oauth.config.CustomUserDetails;
import kr.oyez.security.service.CustomUserService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	private final CustomUserService customUserService;
	private final MemberService memberService;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final HttpSession session;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String memberId = (String) authentication.getPrincipal();
		String memberPwd = (String) authentication.getCredentials();
		
		CustomUserDetails customUserDetails = (CustomUserDetails) customUserService.loadUserByUsername(memberId);
		
		Member member = new Member();
		member.createMember(memberId, memberPwd);
		
		Member memberInfo = memberService.selectMember(member);
		String resultMemberPwd = "";
		
		// 사용자 존재여부 체크
		if(memberInfo == null) {
			throw new UsernameNotFoundException("존재하지 않는 아이디입니다.");
		} else {
			resultMemberPwd = memberInfo.getMemberPwd();
			
			// 비밀번호 체크
			if(!bCryptPasswordEncoder.matches(memberPwd, resultMemberPwd)) {
				
				int failCnt = lockMember(memberId, memberInfo);
				
				throw new BadCredentialsException("비밀번호를 " + failCnt + "회 틀렸습니다. " + "5회 틀릴 경우 계정이 잠깁니다.");
			} else {
				unlockMember(memberId);
			}
			
			session.setAttribute("SessionMember", new SessionMember(memberInfo));
		}
		
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(memberId, memberPwd, customUserDetails.getAuthorities());
		authToken.setDetails(customUserDetails);
		
		return authToken;
	}
	
	private int lockMember(String memberId, Member memberInfo) {
		int failCnt = memberInfo.getFailCnt() + 1;
		String lockYn = failCnt >= 5 ? "Y" : "N";
		String lockDate = failCnt >= 5 ? StringUtils.date() : null;
		
		LockMemberDto lockMember = LockMemberDto.builder()
				.memberId(memberId)
				.failCnt(failCnt)
				.lockYn(lockYn)
				.lockDate(lockDate)
				.build();
		
		memberService.updateFailLogin(lockMember);
		
		return failCnt;
	}
	
	private void unlockMember(String memberId) {
		LockMemberDto lockMember = LockMemberDto.builder()
				.memberId(memberId)
				.failCnt(0)
				.lockYn("N")
				.lockDate(null)
				.build();
		
		memberService.updateFailLogin(lockMember);
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
