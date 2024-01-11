package kr.oyez.exception.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
	
	NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 페이지입니다."),
	
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),
	
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
	
	;
	
	private final HttpStatus status;
	private final String message;
}
