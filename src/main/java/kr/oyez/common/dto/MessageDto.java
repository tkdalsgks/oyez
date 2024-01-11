package kr.oyez.common.dto;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageDto {

	private String message;
	
	private String redirectUri;
	
	private RequestMethod method;
	
	private Map<String, Object> data;
}
