package kr.oyez.common.repository.custom;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.oyez.common.domain.Common;
import kr.oyez.common.domain.QCommon;

public class CommonRepositoryImpl implements CommonRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;
	
	public CommonRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}
	
	QCommon common = QCommon.common;
	
	@Override
	public List<Common> communityFilter() {
		
		List<Common> content = queryFactory.from(common)
				.select(common)
				.where(
						common.commHCd.eq("B01")
				)
				.fetch();
		
		return content;
	}

	@Override
	public List<Common> reviewFilter() {
		
		List<Common> content = queryFactory.from(common)
				.select(common)
				.where(
						common.commHCd.eq("B02")
				)
				.fetch();
		
		return content;
	}
}
