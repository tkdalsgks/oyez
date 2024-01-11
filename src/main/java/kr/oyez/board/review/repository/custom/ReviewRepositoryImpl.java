package kr.oyez.board.review.repository.custom;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.oyez.board.community.domain.Board;
import kr.oyez.board.community.domain.QBoard;
import kr.oyez.board.community.dto.CommunityResponseDto;
import kr.oyez.member.domain.QMember;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;
	
	public ReviewRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}
	
	QBoard board = QBoard.board;
	QMember member = QMember.member;
	
	@Override
	public Slice<CommunityResponseDto> findByAll(Pageable pageable) {
		
		List<CommunityResponseDto> content = queryFactory.from(board)
				.select(Projections.constructor(CommunityResponseDto.class, 
						board.id,
						board.boardSeq,
						board.title,
						board.content,
						board.writerId,
						member.memberNickname,
						member.profileImg,
						board.filter,
						board.rating,
						board.viewCnt,
						board.commentCnt,
						board.likesCnt,
						board.noticeYn,
						board.privateYn,
						board.useYn,
						regDate(),
						updtDate()
						))
				.leftJoin(member).on(board.writerId.eq(member.memberId))
				.where(
						boardSeqEq("2"),
						useYnEq("Y")
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(board.id.desc())
				.fetch();
		
		JPAQuery<Long> total = queryFactory
				.select(board.count())
				.from(board)
				.where(
						boardSeqEq("2"),
						useYnEq("Y")
				);
		
		return PageableExecutionUtils.getPage(content, pageable, total::fetchOne);
	}
	
	@Override
	public Long countBoard() {
		
		return queryFactory.from(board)
				.select(board.count())
				.where(
						boardSeqEq("2"),
						useYnEq("Y")
				)
				.fetchOne();
	}
	
	private DateTemplate<String> regDate() {
		DateTemplate<String> formattedDate = Expressions.dateTemplate(
                String.class
                , "DATE_FORMAT({0}, {1})"
                , board.regDate
                , ConstantImpl.create("%Y%m%d%H%i%s"));
		return formattedDate;
	}
	
	private DateTemplate<String> updtDate() {
		DateTemplate<String> formattedDate = Expressions.dateTemplate(
                String.class
                , "DATE_FORMAT({0}, {1})"
                , board.updtDate
                , ConstantImpl.create("%Y%m%d%H%i%s"));
		return formattedDate;
	}
	
	private BooleanExpression boardSeqEq(String boardSeq) {
		return StringUtils.hasText(boardSeq) ? QBoard.board.boardSeq.eq("2") : null;
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
							board.memberId,
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
						board.boardSeq.eq("2"),
						board.useYn.eq("Y")
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(board.id.desc())
				.fetch();
		
		JPAQuery<Long> total = queryFactory
				.select(board.count())
				.from(board);
		
		return PageableExecutionUtils.getPage(content, pageable, total::fetchOne);
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
								board.memberId,
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

	@Override
	public Page<CommunityResponseDto> findLikes(Pageable pageable) {
		
		List<CommunityResponseDto> content = queryFactory
				.select(
						new QCommunityResponseDto(
								board.id.as("id"),
								board.boardSeq,
								board.title,	
								board.content,
								board.memberId,
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
	}
	*/
}
