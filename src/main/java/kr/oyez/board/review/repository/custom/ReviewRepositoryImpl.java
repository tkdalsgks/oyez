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

import jakarta.persistence.EntityManager;
import kr.oyez.board.community.domain.QBoard;
import kr.oyez.board.review.dto.ReviewRequestDto;
import kr.oyez.board.review.dto.ReviewResponseDto;
import kr.oyez.common.domain.QCommon;
import kr.oyez.member.domain.QMember;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
	
	private final EntityManager em;
	private final JPAQueryFactory queryFactory;
	
	public ReviewRepositoryImpl(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	QCommon common = QCommon.common;
	QBoard board = QBoard.board;
	QMember member = QMember.member;
	
	@Override
	public Slice<ReviewResponseDto> findByAll(Pageable pageable) {
		
		List<ReviewResponseDto> content = queryFactory.from(board)
				.select(Projections.constructor(ReviewResponseDto.class, 
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
						board.hashtag,
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
						boardSeqEq("2"),
						useYnEq("Y"),
						common.commHCd.eq("B02")
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
	public void saveReview(ReviewRequestDto params) {
		
		em.createNativeQuery("INSERT INTO BOARD "
				           + "(BOARD_SEQ, TITLE, CONTENT, MEMBER_ID, TITLE_IMG, "
				           + "FILTER, HASHTAG, RATING, VIEW_CNT, COMMENT_CNT, "
				           + "LIKES_CNT, NOTICE_YN, PRIVATE_YN, USE_YN, REG_DATE) "
				           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
			.setParameter(1, params.getBoardSeq())
			.setParameter(2, params.getTitle())
			.setParameter(3, params.getContent())
			.setParameter(4, params.getWriterId())
			.setParameter(5, params.getTitleImg())
			.setParameter(6, params.getFilter())
			.setParameter(7, params.getHashtag())
			.setParameter(8, params.getRating())
			.setParameter(9, params.getViewCnt())
			.setParameter(10, params.getCommentCnt())
			.setParameter(11, params.getLikesCnt())
			.setParameter(12, params.getNoticeYn())
			.setParameter(13, params.getPrivateYn())
			.setParameter(14, params.getUseYn())
			.setParameter(15, params.getRegDate())
			.executeUpdate();
		
		em.flush();
		em.close();
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
	
	@Override
	public ReviewResponseDto findByBoard(Long id) {
		
		ReviewResponseDto content = queryFactory.from(board)
				.select(Projections.constructor(ReviewResponseDto.class, 
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
						board.hashtag,
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
						common.commHCd.eq("B02")
				)
				.fetchOne();
		
		return content;
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
		return StringUtils.hasText(boardSeq) ? QBoard.board.boardSeq.eq("2") : null;
	}
	
	private BooleanExpression useYnEq(String useYn) {
		return StringUtils.hasText(useYn) ? QBoard.board.useYn.eq("Y") : null;
	}
}
