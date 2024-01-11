package kr.oyez.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDataDTO {

	private String code;
	private String status;
	private String message;
}
