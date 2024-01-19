package kr.oyez.board.community.repository.custom;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.support.PageableExecutionUtils;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.oyez.board.community.domain.QBoard;
import kr.oyez.board.community.dto.CommunityRequestDto;
import kr.oyez.board.community.dto.CommunityResponseDto;
import kr.oyez.common.domain.QCommon;
import kr.oyez.common.utils.StringUtils;
import kr.oyez.member.domain.QMember;

public class CommunityRepositoryImpl implements CommunityRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;
	
	public CommunityRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}
	
	QCommon common = QCommon.common;
	QBoard board = QBoard.board;
	QMember member = QMember.member;
	
	@Override
	public Slice<CommunityResponseDto> findByAllTrend(Pageable pageable) {
		
		List<CommunityResponseDto> content = queryFactory.from(board)
				.select(Projections.constructor(CommunityResponseDto.class, 
						board.id,
						board.boardSeq,
						board.title,
						board.content,
						board.writerId,
						member.memberNickname,
						board.titleImg,
						member.profileImg,
						common.commDCd,
						common.commDNm,
						board.rating,
						board.viewCnt,
						board.commentCnt,
						board.likesCnt,
						board.noticeYn,
						board.privateYn,
						board.useYn,
						regDateTime(),
						updtDateTime()
						))
				.leftJoin(member).on(board.writerId.eq(member.memberId))
				.leftJoin(common).on(board.filter.eq(common.commDCd))
				.where(
						boardSeqEq("1"),
						useYnEq("Y"),
						regDate().between(StringUtils.dateMinus30(), StringUtils.date()),
						common.commHCd.eq("B01")
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(board.viewCnt.desc())
				.fetch();
		
		JPAQuery<Long> total = queryFactory
				.select(board.count())
				.from(board)
				.where(
						boardSeqEq("1"),
						useYnEq("Y"),
						regDate().between(StringUtils.dateMinus30(), StringUtils.date())
				);
		
		return PageableExecutionUtils.getPage(content, pageable, total::fetchOne);
	}
	
	@Override
	public Slice<CommunityResponseDto> findByAllRecent(Pageable pageable) {
		
		List<CommunityResponseDto> content = queryFactory.from(board)
				.select(Projections.constructor(CommunityResponseDto.class, 
						board.id,
						board.boardSeq,
						board.title,
						board.content,
						board.writerId,
						member.memberNickname,
						board.titleImg,
						member.profileImg,
						common.commDCd,
						common.commDNm,
						board.rating,
						board.viewCnt,
						board.commentCnt,
						board.likesCnt,
						board.noticeYn,
						board.privateYn,
						board.useYn,
						regDateTime(),
						updtDateTime()
						))
				.leftJoin(member).on(board.writerId.eq(member.memberId))
				.leftJoin(common).on(board.filter.eq(common.commDCd))
				.where(
						boardSeqEq("1"),
						useYnEq("Y"),
						common.commHCd.eq("B01")
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(board.id.desc())
				.fetch();
		
		JPAQuery<Long> total = queryFactory
				.select(board.count())
				.from(board)
				.where(
						boardSeqEq("1"),
						useYnEq("Y")
				);
		
		return PageableExecutionUtils.getPage(content, pageable, total::fetchOne);
	}
	
	@Override
	public String findByBoardSeq(Long id) {
		
		return queryFactory.from(board)
				.select(board.boardSeq)
				.where(
						board.id.eq(id)
				)
				.fetchOne();
	}

	@Override
	public CommunityResponseDto findByBoard(Long id) {
		
		CommunityResponseDto content = queryFactory.from(board)
				.select(Projections.constructor(CommunityResponseDto.class, 
						board.id,
						board.boardSeq,
						board.title,
						board.content,
						board.writerId,
						member.memberNickname,
						board.titleImg,
						member.profileImg,
						common.commDCd,
						common.commDNm,
						board.rating,
						board.viewCnt,
						board.commentCnt,
						board.likesCnt,
						board.noticeYn,
						board.privateYn,
						board.useYn,
						regDateTime(),
						updtDateTime()
						))
				.leftJoin(member).on(board.writerId.eq(member.memberId))
				.leftJoin(common).on(board.filter.eq(common.commDCd))
				.where(
						board.id.eq(id),
						useYnEq("Y"),
						common.commHCd.eq("B01")
				)
				.fetchOne();
		
		return content;
	}
	
	@Override
	public Long countBoardTrend() {
		
		return queryFactory.from(board)
				.select(board.count())
				.where(
						boardSeqEq("1"),
						useYnEq("Y"),
						regDate().between(StringUtils.dateMinus30(), StringUtils.date())
				)
				.fetchOne();
	}
	
	@Override
	public Long countBoardRecent() {
		
		return queryFactory.from(board)
				.select(board.count())
				.where(
						boardSeqEq("1"),
						useYnEq("Y")
				)
				.fetchOne();
	}
	
	@Override
	public Long countHits(Long id) {
		
		return queryFactory
				.update(board)
				.set(board.viewCnt, board.viewCnt.add(1))
				.where(
						board.id.eq(id)
				)	
				.execute();
	}
	
	@Override
	public Long updateBoard(CommunityRequestDto params) {
		
		return queryFactory
				.update(board)
				.set(board.title, params.getTitle())
				.set(board.content, params.getContent())
				.set(board.updtDate, StringUtils.dateTime())
				.where(
						board.id.eq(params.getBoardId())
				)
				.execute();
	}
	
	private DateTemplate<String> regDate() {
		DateTemplate<String> formattedDate = Expressions.dateTemplate(
                String.class
                , "DATE_FORMAT({0}, {1})"
                , board.regDate
                , ConstantImpl.create("%Y%m%d"));
		return formattedDate;
	}
	
	private DateTemplate<String> regDateTime() {
		DateTemplate<String> formattedDate = Expressions.dateTemplate(
                String.class
                , "DATE_FORMAT({0}, {1})"
                , board.regDate
                , ConstantImpl.create("%Y%m%d%H%i%s"));
		return formattedDate;
	}
	
	private DateTemplate<String> updtDateTime() {
		DateTemplate<String> formattedDate = Expressions.dateTemplate(
                String.class
                , "DATE_FORMAT({0}, {1})"
                , board.updtDate
                , ConstantImpl.create("%Y%m%d%H%i%s"));
		return formattedDate;
	}
	
	private BooleanExpression boardSeqEq(String boardSeq) {
		return StringUtils.hasText(boardSeq) ? QBoard.board.boardSeq.eq("1") : null;
	}
	
	private BooleanExpression useYnEq(String useYn) {
		return StringUtils.hasText(useYn) ? QBoard.board.useYn.eq("Y") : null;
	}
	
	/*
	@Override
	public Slice<CommunityResponseDto> findByAll(Pageable pageable) {
		
		List<CommunityResponseDto> content = queryFactory
				.select(
						new QCommunityResponseDto(
							board.id.as("id"),
							board.boardSeq,
							board.title,	
							board.content,
							board.writerId,
							board.writer,
							board.filter,
							board.rating,
							board.viewCnt,
							board.commentCnt,
							board.likesCnt,
							board.noticeYn,
							board.privateYn,
							board.useYn,
							board.regDate,
							board.updtDate
				))
				.from(board)
				.where(
						board.useYn.eq("Y")
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize() + 1)
				.orderBy(board.id.desc())
				.fetch();
		
		boolean hasNext = false;
		if(content.size() > pageable.getPageSize()) {
			hasNext = true;
            content.remove(pageable.getPageSize());
		}
		
		return new SliceImpl<>(content, pageable, hasNext);
	}
	*/
	/*
	@Override
	public Page<CommunityResponseDto> findNotice(Pageable pageable) {
		
		List<CommunityResponseDto> content = queryFactory
				.select(
						new QCommunityResponseDto(
								board.id.as("id"),
								board.boardSeq,
								board.title,	
								board.content,
								board.writerId,
								board.writer,
								board.filter,
								board.rating,
								board.viewCnt,
								board.commentCnt,
								board.likesCnt,
								board.noticeYn,
								board.privateYn,
								board.useYn,
								board.regDate,
								board.updtDate
				))
				.from(board)
				.where(
						board.noticeYn.eq("Y"),
						board.useYn.eq("Y")
				)
				.orderBy(board.id.desc())
				.offset(pageable.getOffset())
				.limit(2)
				.fetch();
		
		JPAQuery<Long> total = queryFactory
				.select(board.count())
				.from(board);
		
		return PageableExecutionUtils.getPage(content, pageable, total::fetchOne);
	}
	
	/*
	@Override
	public Page<CommunityResponseDto> findLikes(Pageable pageable) {
		
		List<CommunityResponseDto> content = queryFactory
				.select(
						new QCommunityResponseDto(
								board.id.as("id"),
								board.boardSeq,
								board.title,	
								board.content,
								board.writerId,
								board.writer,
								board.filter,
								board.rating,
								board.viewCnt,
								board.commentCnt,
								board.likesCnt,
								board.noticeYn,
								board.privateYn,
								board.useYn,
								board.regDate,
								board.updtDate
				))
				.from(board)
				.where(
						board.noticeYn.eq("Y"),
						board.useYn.eq("Y")
				)
				.orderBy(board.likesCnt.desc())
				.offset(pageable.getOffset())
				.limit(2)
				.fetch();
		
		JPAQuery<Long> total = queryFactory
				.select(board.count())
				.from(board);
		
		return PageableExecutionUtils.getPage(content, pageable, total::fetchOne);
	}*/
}
