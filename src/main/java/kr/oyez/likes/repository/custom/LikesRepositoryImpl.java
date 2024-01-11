package kr.oyez.likes.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.oyez.board.community.domain.QBoard;
import kr.oyez.common.utils.StringUtils;
import kr.oyez.likes.entity.Likes;
import kr.oyez.likes.entity.QLikes;
import kr.oyez.member.domain.QMember;

public class LikesRepositoryImpl implements LikesRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;
	
	public LikesRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
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
		// SELECT COUNT(1) FROM BOARD_LIKES WHERE BOARD_ID = #{ boardId } AND USER_ID = #{ userId } AND USE_YN = 'Y'
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
	public Long updateCountLikes(Likes params) {
		// UPDATE BOARD B
		//SET    B.LIKES_CNT = ( SELECT COUNT(1)
		//		                FROM   BOARD_LIKES L
		//		                WHERE  1=1
		//		                AND    L.USE_YN = 'Y'
		//		                AND    L.BOARD_ID = #{ boardId } )
		//		WHERE  1=1
		//		AND    B.DELETE_YN = 0
		//		AND    B.ID = #{ boardId }
		return null;/*queryFactory
				.update(board)
				.set(board.likesCnt, 
						.selectTodayLikes(likes.count())
						.from(likes)
						.where(
								likes.useYn.eq("Y"),
								likes.boardId.eq(params.getBoardId())
						)
				)
				.where(
						board.useYn.eq("Y")
				)
				.execute();
				*/
	}

}
