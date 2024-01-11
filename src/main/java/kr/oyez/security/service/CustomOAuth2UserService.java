package kr.oyez.security.service;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import kr.oyez.common.domain.LoginLog;
import kr.oyez.common.utils.StringUtils;
import kr.oyez.member.domain.Member;
import kr.oyez.member.domain.Role;
import kr.oyez.member.dto.SessionMember;
import kr.oyez.security.oauth.config.OAuthAttributes;
import kr.oyez.security.repository.SecurityRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final SecurityRepository securityRepository;
	private final HttpSession session;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);
		
		// OAuth2 서비스 ID
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		// OAuth2 로그인 진행 시 키가 되는 필드 값
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		
		// OAuth2UserService
		OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
		Member member = saveOrUpdate(attributes);
		session.setAttribute("SessionMember", new SessionMember(member));
		
		return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
				attributes.getAttributes(),
				attributes.getNameAttributeKey());
	}
	
	// 유저 생성 및 수정 서비스 로직
	private Member saveOrUpdate(OAuthAttributes attributes) {
		
		Member member = securityRepository.findByMemberEmail(attributes.getMemberEmail());
        if(member != null) {
        	log.info("-----");
        	log.info("@@@ [" + attributes.getProvider() + "] 이미 가입된 사용자입니다. - {}", attributes.getMemberEmail());
            log.info("-----");
            member = securityRepository.findByMemberEmail(attributes.getMemberEmail());
            
            if("Y".equals(member.getUseYn())) {
            	if("N".equals(member.getLockYn())) {
            		
            		LoginLog loginLog = LoginLog.builder()
            				.loginId(member.getMemberId())
            				.accessIp(getServerIp())
            				.loginDate(StringUtils.dateTime())
            				.build();
            		
            		securityRepository.insertLoginLog(loginLog);
            	} else {
            		throw new LockedException("해당 계정이 잠겼습니다. 비밀번호 찾기를 진행하여 새로운 비밀번호로 변경하세요.");
            	}            	
            } else {
            	throw new UsernameNotFoundException("직접 탈퇴했거나 관리자가 추방한 계정입니다.");
            }
        } else {
        	member = attributes.Entity();
        	
        	SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        	Date date = new Date();
        	String today = sdf.format(date);
        	member.nickname(member.getMemberNickname() + "_" + today + (int)(Math.random() * 100000 + 1));
        	
        	saveMember(member);
            
        	log.info("-----");
        	log.info("@@@ [" + attributes.getProvider() + "] 정상 가입 완료되었습니다. - {}", attributes.getMemberEmail());
        	log.info("-----");
        }

        return member;
	}

	private void saveMember(Member member) {
		Member saveMember = Member.builder()
				.memberId(member.getMemberId())
				.memberEmail(member.getMemberEmail())
				.memberNickname(member.getMemberNickname())
				.provider(member.getProvider())
				.role(Role.GUEST)
				.useYn("Y")
				.lockYn("N")
				.joinDate(StringUtils.date())
				.regUser("SYSTEM")
				.regDate(StringUtils.dateTime())
				.updtUser("SYSTEM")
				.updtDate(StringUtils.dateTime())
				.pwdUpdtDate(StringUtils.dateTime())
				.build();
		
		securityRepository.save(saveMember);
	}
	
	// 현재 접속한 로컬 IPv4 주소 반환
	public String getServerIp() {
	    try {
	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        while(interfaces.hasMoreElements()) {
	            NetworkInterface ni = interfaces.nextElement();
	            
	            //MAC 주소 조회
	            byte[] hardwareAddress = ni.getHardwareAddress();
	            
	            //Virtual Box의 MAC 주소 형태의 차이로 구별
	            boolean virtual = false;
	            if(hardwareAddress != null) {
	                //hardwareAddress와 localIp 값을 각각 출력해보고
	                //Virtual Box IP가 아닐 때의 MAC 주소를 아래 조건문을 통해 필터링
	                if(hardwareAddress[0] != -44)
	                    virtual = true;
	            }
	            if(virtual) continue;
					
	            for(InterfaceAddress addr : ni.getInterfaceAddresses()) {
	                InetAddress address = addr.getAddress();
	                if(address.isSiteLocalAddress()) {
	                    //주소 문자열에서 /가 나오는 것을 치환
	                    String localIp = addr.getAddress().toString().replace("/", "");
	                    return localIp;
	                }
	            }
	        }
	    } catch (SocketException e) {
	        e.printStackTrace();
	    }
			
	    //Virtual Box가 없는 경우 위에서 걸리지 않는 것으로 보임
	    //따라서, 위에서 반환되지 않은 경우
	    //서버 IP를 조회하는 기존 코드를 추가 작성
	    try {
	        return InetAddress.getLocalHost().getHostAddress();
	    } catch (UnknownHostException e) {
	        e.printStackTrace();
	    }
	    
	    return null;
	}
}
