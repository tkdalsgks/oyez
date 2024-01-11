package kr.oyez.exception;

import kr.oyez.exception.dto.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

	private final ErrorCode errorCode;
}
