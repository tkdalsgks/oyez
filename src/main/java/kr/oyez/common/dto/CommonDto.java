package kr.oyez.common.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CommonDto {
	
	private String commHCd;
	private String commHNm;
	private String commDCd;
	private String commDNm;
	private String useYn;
	private LocalDateTime IDate;

}
