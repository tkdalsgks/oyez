package kr.oyez.comment.repository.custom;

import java.util.List;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.oyez.comment.domain.QComment;
import kr.oyez.comment.dto.CommentResponseDto;
import kr.oyez.member.domain.QMember;

public class CommentRepositoryImpl implements CommentRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;
	
	public CommentRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}
	
	QComment comment = QComment.comment;
	QMember member = QMember.member;
	
	@Override
	public List<CommentResponseDto> findByAll(Long boardId) {
		
		List<CommentResponseDto> content = queryFactory.from(comment)
				.select(Projections.constructor(CommentResponseDto.class, 
						comment.id,
						comment.boardId,
						comment.content,
						member.memberId,
						member.memberNickname,
						member.profileImg,
						comment.rating,
						comment.deleteYn,
						regDate(),
						updtDate()
						))
				.leftJoin(member).on(comment.memberId.eq(member.memberId))
				.where(
						boardIdEq(boardId)
				)
				.orderBy(comment.id.desc())
				.fetch();
					
		return content;
	}
	
	@Override
	public Long countComment(Long boardId) {
		return queryFactory.from(comment)
				.select(comment.id.count())
				.where(
						comment.boardId.eq(boardId)
				)
				.fetchOne();
	}
	
	private DateTemplate<String> regDate() {
		DateTemplate<String> formattedDate = Expressions.dateTemplate(
                String.class
                , "DATE_FORMAT({0}, {1})"
                , comment.regDate
                , ConstantImpl.create("%Y%m%d%H%i%s"));
		return formattedDate;
	}
	
	private DateTemplate<String> updtDate() {
		DateTemplate<String> formattedDate = Expressions.dateTemplate(
                String.class
                , "DATE_FORMAT({0}, {1})"
                , comment.updtDate
                , ConstantImpl.create("%Y%m%d%H%i%s"));
		return formattedDate;
	}
	
	private BooleanExpression boardIdEq(Long boardId) {
		return boardId != null ? QComment.comment.boardId.eq(boardId) : null;
	}
}
