package kr.oyez.member.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.nimbusds.jose.shaded.gson.JsonObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.oyez.common.utils.ScriptUtils;
import kr.oyez.common.utils.StringUtils;
import kr.oyez.member.domain.Member;
import kr.oyez.member.domain.MemberCertified;
import kr.oyez.member.domain.Role;
import kr.oyez.member.dto.CreateMemberDto;
import kr.oyez.member.dto.LockMemberDto;
import kr.oyez.member.dto.SessionMember;
import kr.oyez.member.dto.UpdateMemberDto;
import kr.oyez.member.repository.MemberCertifiedRepository;
import kr.oyez.member.repository.MemberRepository;
import kr.oyez.security.repository.SecurityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
	
	@Value("${cloud.aws.s3.bucket}")
    public String bucket;

	private final MemberRepository memberRepository;
	private final MemberCertifiedRepository memberCertifiedRepository;
	private final SecurityRepository securityRepository;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final AmazonS3 amazonS3;
	private final HttpSession session;
	
	@Transactional
	public Long joinMember(CreateMemberDto createMember) {
		
		duplicateMember(createMember);
		
		// 회원가입
    	String rawPwd = createMember.getMemberPwd();
    	String encPwd = bCryptPasswordEncoder.encode(rawPwd);
		saveMember(createMember, encPwd);
		
		return createMember.getId();
	}

	private Member duplicateMember(CreateMemberDto createMember) {
		Member newMember = securityRepository.findByMemberId(createMember.getMemberId());
		if(newMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}
		return newMember;
	}
	
	public Member selectMember(Member member) {
		return memberRepository.findByMemberId(member.getMemberId());
	}

	public void updateFailLogin(LockMemberDto params) {
		memberRepository.updateFailLogin(params);
	}
	
	public Member findByMemberId(String memberEmail) {
		return memberRepository.findByMemberId(memberEmail);
	}
	
	public Member findByMemberPwd(String memberId) {
		return memberRepository.findByMemberPwd(memberId);
	}
	
	public boolean updateProfile(Member member) {
		long queryResult = 0;
		
		if(getMemberId() != null) {
			UpdateMemberDto updateProfile = UpdateMemberDto.builder()
					.memberId(getMemberId())
					.memberNickname(member.getMemberNickname())
					.memberEmail(member.getMemberEmail())
					.updtUser(member.getMemberId())
					.profileUpdtDate(StringUtils.dateTime())
					.build();
			
			queryResult = memberRepository.updateProfile(updateProfile);
		}

		return (queryResult == 1) ? true : false;
	}
	
	public MemberCertified selectCertifiedMember(String memberId) {
		return memberCertifiedRepository.selectCertifiedMember(memberId);
	}
	
	private void saveMember(CreateMemberDto createMember, String encPwd) {
		Member saveMember = Member.builder()
				.memberId(createMember.getMemberId())
				.memberEmail(createMember.getMemberEmail())
				.memberPwd(encPwd)
				.memberNickname(createMember.getMemberNickname())
				.provider("general")
				.role(Role.GUEST)
				.useYn("Y")
				.lockYn("N")
				.joinDate(StringUtils.date())
				.regUser("SYSTEM")
				.regDate(StringUtils.dateTime())
				.updtUser("SYSTEM")
				.updtDate(StringUtils.dateTime())
				.pwdUpdtDate(StringUtils.dateTime())
				.profileUpdtDate(StringUtils.dateTime())
				.build();
		
		memberRepository.save(saveMember);
	}
	
	public String upload(HttpServletRequest request, HttpServletResponse response, MultipartHttpServletRequest upload) throws Exception {
    	JsonObject jsonObj = new JsonObject();
		PrintWriter printWriter = null;
		OutputStream out = null;
		MultipartFile file = upload.getFile("upload");
		if(file != null) {
			if(file.getSize() > 0 && StringUtils.isNotBlank(file.getName())) {
				if(file.getContentType().toLowerCase().startsWith("image/")) {
					try {
						String fileName = file.getName();
						byte[] bytes = file.getBytes();
						String uploadPath = request.getServletContext().getRealPath("/img");
						File uploadFile = new File(uploadPath);
						if(!uploadFile.exists()) {
							uploadFile.mkdirs();
						}
						fileName = getUuid();
						uploadPath = uploadPath + "/" + fileName;
						out = new FileOutputStream(new File(uploadPath));
						
						out.write(bytes);
						
						File files = new File(System.getProperty("user.dir") + "/" + fileName);
						file.transferTo(files);
				    	uploadOnS3(fileName, files);
						
						printWriter = response.getWriter();
						response.setContentType("text/html");
						String fileUrl = "https://oyez-webservice.s3.ap-northeast-2.amazonaws.com/images/" + getMemberId() + "/" + fileName;
						
						// 프로필사진 업데이트
						log.info("@@@ " + getMemberId());
						updateProfileImg(fileUrl);
						
						jsonObj.addProperty("uploaded", 1);
						jsonObj.addProperty("fileName", fileName);
						jsonObj.addProperty("url", fileUrl);
						
						printWriter.print(jsonObj);
						files.delete();
						
						ScriptUtils.backPage(response);
					} catch(IOException e) {
						e.printStackTrace();
					} finally {
						if(out != null) {
							out.close();
						}
						if(printWriter != null) {
							printWriter.close();
						}
					}
				}
			}
		}
		return null;
    }

	private void updateProfileImg(String fileUrl) {
		Member updateProfileImg = Member.builder()
				.memberId(getMemberId())
				.profileImg(fileUrl)
				.updtUser(getMemberId())
				.updtDate(StringUtils.dateTime())
				.build();
		
		memberRepository.updateProfileImg(updateProfileImg);
	}
    
    private static String getUuid() {
    	int length = 10;
		String result = "";

		Random r = new Random();

		int intValue;
		char charValue;
		for (int i = 0; i < length / 2; i++) {
			intValue = (int) (Math.random() * 9) + 1;
			charValue = (char) (r.nextInt(26) + 65);

			result += (charValue + Integer.toString(intValue));
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		result = sdf.format(new Date()) + "_" + result;
		return result;
    }
    
    private void uploadOnS3(final String findName, final File file) {
        // AWS S3 전송 객체 생성
        final TransferManager transferManager = TransferManagerBuilder.standard()
                                                .withS3Client(this.amazonS3).build();
        // 요청 객체 생성
        final PutObjectRequest request = new PutObjectRequest(bucket + "/images/" + getMemberId(), findName, file);
        
        // 업로드 시도
        final Upload upload =  transferManager.upload(request);

        try {
            upload.waitForCompletion();
        } catch (AmazonClientException | InterruptedException amazonClientException) {
            amazonClientException.printStackTrace();
        }
    }
    
    public String getMemberId() {
    	SessionMember sessionMember = (SessionMember) session.getAttribute("SessionMember");
    	String result = sessionMember.getMemberId();
    	
    	return result;
    }
}
