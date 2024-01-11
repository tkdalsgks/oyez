package kr.oyez.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.lang.Nullable;
import org.springframework.ui.Model;

import kr.oyez.common.dto.MessageDto;
import kr.oyez.common.dto.SearchDto;

public class StringUtils {

	public static String date() {
		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		return date;
	}
	
	public static String dateMinus1() {
		String date = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		return date;
	}
	
	public static String dateMinus30() {
		String date = LocalDateTime.now().minusDays(30).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		return date;
	}
	
	public static String time() {
		String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
		
		return time;
	}
	
	public static String dateTime() {
		String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
		
		return date + time;
	}
	
	public static LocalDateTime formatDate(String date) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime dateTime = LocalDateTime.parse(date, format);
		
		return dateTime;
	}
	
	public static LocalDateTime formatDateTime(String date) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime dateTime = LocalDateTime.parse(date, format);
		
		return dateTime;
	}
	
	public static boolean isNotBlank(final CharSequence cs) {
		
		return !isBlank(cs);
	}
	
	public static boolean isBlank(final CharSequence cs) {
		
		int strLen;
		if (cs != null && (strLen = cs.length()) != 0) {
			for(int i = 0; i < strLen; ++i) {
				if (!Character.isWhitespace(cs.charAt(i))) {
					return false;
				}
			}
			
		}
		return true;
	}
	
	public static boolean hasText(@Nullable String str) {
		return (str != null && !str.isBlank());
	}
	
	/**
	 * 성공/실패 메세지를 띄우고 해당 주소 리다이렉트
	 */
	public static String showMessageAndRedirect(final MessageDto message, Model model) {
		model.addAttribute("message", message);
		
		return "common/messageRedirect";
	}
	
	/** 
	 * 쿼리 스트링 파라미터를 Map에 담아 반환
	 */
    public static Map<String, Object> queryParamsToMap(final SearchDto queryParams) {
        Map<String, Object> data = new HashMap<>();
        data.put("page", queryParams.getPage());
        data.put("recordSize", queryParams.getRecordSize());
        data.put("pageSize", queryParams.getPageSize());
        data.put("keyword", queryParams.getKeyword());
        data.put("searchType", queryParams.getSearchType());
        return data;
    }
}
