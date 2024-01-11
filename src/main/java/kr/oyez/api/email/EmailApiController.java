package kr.oyez.api.email;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import kr.oyez.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EmailApiController {
	
	private final EmailService emailService;

	@ResponseBody
	@PostMapping("/check/mail")
	public String checkMail(@RequestParam("memberEmail") String memberEmail) throws Exception {
		
		String code = emailService.sendSimpleMessage(memberEmail);
		log.info("@@@ [MAIL] Email Check " + memberEmail);
		log.info("@@@ [MAIL] CODE " + code);
		
		return code;
	}
	
	@ResponseBody
	@PostMapping("/check/verifyCode")
	public int verifyCode(@RequestParam("code") String code) {
		int result = 0;
		log.info("@@@ [MAIL] CODE " + code);
		log.info("@@@ [MAIL] CODE Match " + EmailService.ePw.equals(code));
		
		if(EmailService.ePw.equals(code)) {
			result = 1;
		}
		
		return result;
	}
}
