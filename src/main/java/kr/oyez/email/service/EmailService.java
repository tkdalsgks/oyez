package kr.oyez.email.service;

import java.util.Random;
import java.util.UUID;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import kr.oyez.common.utils.StringUtils;
import kr.oyez.member.domain.Member;
import kr.oyez.member.domain.MemberCertified;
import kr.oyez.member.repository.MemberCertifiedRepository;
import kr.oyez.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final MemberRepository memberRepository;
	private final MemberCertifiedRepository memberCertifiedRepository;
	private final JavaMailSender emailSender;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	public static final String ePw = createKey();
	public static final String ePwRand = UUID.randomUUID().toString();
	
	private MimeMessage createMessage(String to) throws Exception {
		
		MimeMessage message = emailSender.createMimeMessage();
		
		message.addRecipients(RecipientType.TO, to);
		message.setSubject("[OYEZ] 이메일 인증 메일입니다.");
		
		String msg = "";
		msg += "<div align='center' style='margin: 20px;'>";
		msg += "<h3>OYEZ</h3>";
		msg += "<br>";
		msg += "<p>아래 CODE를 입력해주세요.<p>";
		msg += "<br>";
		msg += "<div style='border: 1px solid black; font-family: 'Gowun Dodum', sans-serif;'>";
		msg += "<div style='font-size: 130%;'>";
		msg += "<h3>CODE : <strong>";
		msg += ePw + "</strong></h3></div></div></div>";
		message.setText(msg, "UTF-8", "html");
		message.setFrom(new InternetAddress("alstkdgks@gmail.com", "OYEZ"));
		
		return message;
	}
	
	public void certifiedEmail(Member member) throws Exception {
		
		MimeMessage message = emailSender.createMimeMessage();
		
		message.addRecipients(RecipientType.TO, member.getMemberEmail());
		message.setSubject("[OYEZ] 계정권한 인증 메일입니다.");
		
		String msg = "";
		msg += "<div align='center' style='margin: 20px;'>";
		msg += "<h3>OYEZ</h3>";
		msg += "<br>";
		msg += "<p>아래 [이메일 인증 확인] 버튼을 눌러주세요.<p>";
		msg += "<p></p>";
		msg += "<a href='http://localhost:8081/certified?email=" + member.getMemberEmail() + "&ePw=" + ePwRand;
		msg += "' target='_blank'>이메일 인증 확인</a>";
		message.setText(msg, "UTF-8", "html");
		message.setFrom(new InternetAddress("alstkdgks@gmail.com", "OYEZ"));
		
		try {
			// 계정 권한 인증
			MemberCertified certifiedEmail = MemberCertified.builder()
					.memberId(member.getMemberId())
					.certifiedCode(ePwRand)
					.certifiedYn("N")
					.regDate(StringUtils.dateTime())
					.UpdtDate(null)
					.build();
			
			memberCertifiedRepository.save(certifiedEmail);
			
			// 메일 전송
			emailSender.send(message);
		} catch(MailException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}
	
	public MimeMessage updateUserPwd(Member member) throws Exception {
		
		String newPwd = ePw;
		String encPwd = bCryptPasswordEncoder.encode(newPwd);
		
		MimeMessage message = emailSender.createMimeMessage();
		
		message.addRecipients(RecipientType.TO, member.getMemberId());
		message.setSubject("[OYEZ] 임시 비밀번호 발급 메일입니다.");
		
		String msg = "";
		msg += "<div align='center' style='margin: 20px;'>";
		msg += "<h3>OYEZ</h3>";
		msg += "<br>";
		msg += "<p>아래 발급된 임시 비밀번호를 이용하여 로그인 하세요.<p>";
		msg += "<br>";
		msg += "<div style='border: 1px solid black; font-family: 'Gowun Dodum', sans-serif;'>";
		msg += "<div style='font-size: 130%;'>";
		msg += "<h3><strong>";
		msg += ePw + "</strong></h3></div></div></div>";
		message.setText(msg, "UTF-8", "html");
		message.setFrom(new InternetAddress("alstkdgks@gmail.com", "OYEZ"));
		
		try {
			// 임시 비밀번호 업데이트
			Member updatePwd = Member.builder()
					.memberPwd(encPwd)
					.pwdUpdtDate(StringUtils.date())
					.updtUser("SYSTEM")
					.updtDate(StringUtils.date())
					.build();
			
			memberRepository.updateMemberPwd(updatePwd);
			
			// 메일 전송
			emailSender.send(message);
		} catch(MailException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
		
		return message;
	}
	
	public static String createKey() {
		StringBuffer key = new StringBuffer();
		Random rnd = new Random();
		
		for(int i = 0; i < 8; i++) {	// 인증코드 8자리
			int index = rnd.nextInt(3);	// 0-2 까지 랜덤
			
			switch(index) {
			case 0:
				key.append((char) ((int) (rnd.nextInt(26)) + 97));
				// a-z  (ex. 1+97+98 -> (char)98 = 'b')
				break;
				
			case 1:
				key.append((char) ((int) (rnd.nextInt(26)) + 65));
				// A-Z
				break;
				
			case 2:
				key.append((rnd.nextInt(10)));
				// 0-9
				break;
			}
		}
		return key.toString();
	}
	
	public String sendSimpleMessage(String to) throws Exception {
		MimeMessage message = createMessage(to);
		
		try {
			emailSender.send(message);
		} catch(MailException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
		
		return ePw;
	}

	public MemberCertified selectCertifiedMember(String memberId) {
		return memberCertifiedRepository.selectCertifiedMember(memberId);
	}
	
	public void successCertifiedEmail(MemberCertified memberCertified) {
		memberCertifiedRepository.successCertifiedEmail(memberCertified);
	}

	public void successCertifiedRole(Member member) {
		memberCertifiedRepository.successCertifiedRole(member);
	}
}
