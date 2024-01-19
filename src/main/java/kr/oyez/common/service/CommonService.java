package kr.oyez.common.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.oyez.common.domain.Common;
import kr.oyez.common.repository.CommonRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonService {
	
	private final CommonRepository commonRepository;
	
	public List<Common> communityFilter() {
		
		return commonRepository.communityFilter();
	}
	
	public List<Common> reviewFilter() {
		
		return commonRepository.reviewFilter();
	}
}
