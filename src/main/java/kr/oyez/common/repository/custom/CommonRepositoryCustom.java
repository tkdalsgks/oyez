package kr.oyez.common.repository.custom;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.oyez.common.domain.Common;

@Repository
public interface CommonRepositoryCustom {
	
	List<Common> communityFilter();
	
	List<Common> reviewFilter();
}
