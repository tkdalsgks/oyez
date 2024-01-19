package kr.oyez.aws.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.google.gson.JsonObject;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.oyez.member.dto.SessionMember;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    private final AmazonS3 amazonS3;
    private final HttpSession session;
    
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
						
						jsonObj.addProperty("uploaded", 1);
						jsonObj.addProperty("fileName", fileName);
						jsonObj.addProperty("url", fileUrl);
						
						printWriter.print(jsonObj);
						files.delete();
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
