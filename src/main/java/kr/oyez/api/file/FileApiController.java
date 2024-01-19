package kr.oyez.api.file;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import kr.oyez.aws.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FileApiController {
	
	private final FileService fileService;
	
	@Transactional
	@PostMapping("/upload")
	public String upload(HttpServletRequest request, HttpServletResponse response, MultipartHttpServletRequest upload) throws Exception {
		log.info("@@@ [AWS S3] File Upload");
		
		return fileService.upload(request, response, upload);
	}
}
