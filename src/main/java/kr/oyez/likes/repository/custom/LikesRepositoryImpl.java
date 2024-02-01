package kr.oyez.likes.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import kr.oyez.board.community.domain.QBoard;
import kr.oyez.common.utils.StringUtils;
import kr.oyez.likes.entity.Likes;
import kr.oyez.likes.entity.QLikes;
import kr.oyez.member.domain.QMember;

public class LikesRepositoryImpl implements LikesRepositoryCustom {
	
	private final EntityManager em;
	private final JPAQueryFactory queryFactory;
	
	public LikesRepositoryImpl(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	QLikes likes = QLikes.likes;
	QMember member = QMember.member;
	QBoard board = QBoard.board;
	
	@Override
	public Long selectTodayLikes(String memberId) {
		
		return queryFactory.from(likes)
				.select(likes.count())
				.where(
						likes.useYn.eq("Y"),
						likes.memberId.eq(memberId),
						likes.regDate.between(StringUtils.dateMinus1(), StringUtils.date())
				)
				.fetchOne();
	}

	@Override
	public Long countBoardLikes(Likes params) {
		
		return queryFactory.from(likes)
				.select(likes.count())
				.where(
						likes.useYn.eq("Y"),
						likes.boardId.eq(params.getBoardId()),
						likes.memberId.eq(params.getMemberId())
				)
				.fetchOne();
	}
	
	@Override
	public void saveLikes(Likes params) {
		
		em.createNativeQuery("INSERT INTO BOARD_LIKES (BOARD_ID, MEMBER_ID, REG_DATE, USE_YN) "
				           + "VALUES (?, ?, ?, ?)")
			.setParameter(1, params.getBoardId())
			.setParameter(2, params.getMemberId())
			.setParameter(3, params.getRegDate())
			.setParameter(4, params.getUseYn())
			.executeUpdate();
		
		em.flush();
		em.close();
	}
	
	@Override
	public Long deleteLikes(Likes params) {
		
		return queryFactory
				.update(likes)
				.set(likes.useYn, "N")
				.where(
						likes.boardId.eq(params.getBoardId()),
						likes.memberId.eq(params.getMemberId())
				)
				.execute();
	}
	
	@Override
	public void updateCountLikes(Likes params) {
		
		em.createNativeQuery("UPDATE BOARD A "
				           + "SET A.LIKES_CNT = ( SELECT COUNT(1) FROM BOARD_LIKES B WHERE B.USE_YN = 'Y' AND B.BOARD_ID = ? ) "
				           + "WHERE 1=1 "
				           + "AND A.USE_YN = 'Y' "
				           + "AND A.ID = ?")
			.setParameter(1, params.getBoardId())
			.setParameter(2, params.getBoardId())
			.executeUpdate();
		
		em.flush();
		em.close();
	}
}
