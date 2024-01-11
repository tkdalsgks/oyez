package kr.oyez.common.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class LocalDateTimeDto {

	private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	private String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
	
	public String date(String date) {
		return date;
	}
	
	public String time(String time) {
		return time;
	}
	
	public String dateTime(String date, String time) {
		return date + time;
	}
}
