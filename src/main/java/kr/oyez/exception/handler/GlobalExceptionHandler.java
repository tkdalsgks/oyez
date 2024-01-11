package kr.oyez.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.client.HttpServerErrorException.ServiceUnavailable;

import kr.oyez.exception.CustomException;

@Controller
@ControllerAdvice
public class GlobalExceptionHandler {
	
	private String PATH = "/error/";
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@ExceptionHandler(CustomException.class)
	protected String handleCustomException(final CustomException e) {
		log.error("handleCustomException: {}", e.getErrorCode());
		/* return ErrorResponse.toResponseEntity(e.getErrorCode()); */
		return PATH + "common";
	}
	
	/**
	 * 400 BAD_REQUEST: 잘못된 요청
	 * @param e
	 * @return
	 */
	@ExceptionHandler(BadRequest.class)
	protected String handleBadRequestException(final BadRequest e) {
		log.error("[400 ERROR] BAD_REQUEST: {}", e.getMessage());
		return PATH + "400";
	}
	
	/**
	 * 401 UNAUTHORIZED: 로그인 권한 에러
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Unauthorized.class)
	protected String handleUnauthorizedException(final Unauthorized e) {
		log.error("[401 ERROR] UNAUTHORIZED: {}", e.getMessage());
		return PATH + "401";
	}
	
	/**
	 * 403 FORBIDDEN: 권한 에러
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Forbidden.class)
	protected String handleForbiddenException(final Forbidden e) {
		log.error("[403 ERROR] FORBIDDEN: {}", e.getMessage());
		return PATH + "403";
	}
	
	/**
	 * 404 NOT_FOUND: 페이지를 찾을 수 없음
	 * @param e
	 * @return
	 */
	@ExceptionHandler(NotFound.class)
	protected String handleNotFoundException(final NotFound e) {
		log.error("[404 ERROR] NOT_FOUND: {}", e.getMessage());
		return PATH + "404";
	}
	
	/**
	 * 405 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출
	 * @param e
	 * @return
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected String handlehttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
		log.error("[405 ERROR] METHOD_NOT_ALLOWED: {}", e.getMessage());
		/* return ErrorResponse.toResponseEntity(ErrorCode.METHOD_NOT_ALLOWED); */
		return PATH + "405";
	}
	
	/**
	 * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
	 * @param e
	 * @return
	 */
	@ExceptionHandler(InternalServerError.class)
	protected String handleInternalServerErrorException(final InternalServerError e) {
		log.error("[500 ERROR] INTERNAL_SERVER_ERROR: {}", e.getMessage());
		/* return ErrorResponse.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR); */
		return PATH + "500";
	}
	
	/**
	 * 503 SERVICE_UNAVAILABLE: 서비스 점검
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ServiceUnavailable.class)
	protected String handleServiceUnavailableException(final ServiceUnavailable e) {
		log.error("[500 ERROR] INTERNAL_SERVER_ERROR: {}", e.getMessage());
		/* return ErrorResponse.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR); */
		return PATH + "500";
	}
}
