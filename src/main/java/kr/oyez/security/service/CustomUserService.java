package kr.oyez.security.service;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import kr.oyez.common.domain.LoginLog;
import kr.oyez.common.utils.StringUtils;
import kr.oyez.member.domain.Member;
import kr.oyez.member.service.MemberService;
import kr.oyez.security.oauth.config.CustomUserDetails;
import kr.oyez.security.repository.SecurityRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {

	private final SecurityRepository securityRepository;
	private final MemberService userService;
	
	private final HttpSession session;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
		
		Member member = securityRepository.findByMemberId(memberId);
		if(member != null) {
			if(member.getUseYn().equals("Y")) {
				if(member.getLockYn().equals("N")) {
					log.info("-----");
		        	log.info("@@@ [LOGIN] 로그인을 진행합니다. - {}", member.getMemberId());
		            log.info("-----");
					
		            // 로그인 로그 이력
					insertLoginLog(member);
					
					return new CustomUserDetails(member);
				} else {			
					throw new LockedException("해당 계정이 잠겼습니다.\n비밀번호 찾기를 진행하여 새로운 비밀번호로 변경하세요.");
				}
			} else {
				throw new UsernameNotFoundException("직접 탈퇴했거나 관리자가 추방한 계정입니다.");
			}
		} else {
			throw new UsernameNotFoundException("존재하지 않는 아이디입니다.");
		}
		
	}

	private void insertLoginLog(Member member) {
		LoginLog loginLog = LoginLog.builder()
				.loginId(member.getMemberId())
				.accessIp(getServerIp())
				.loginDate(StringUtils.dateTime())
				.build();
		
		securityRepository.insertLoginLog(loginLog);
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
