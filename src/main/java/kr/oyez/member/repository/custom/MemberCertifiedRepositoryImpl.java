package kr.oyez.member.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kr.oyez.member.domain.Member;
import kr.oyez.member.domain.MemberCertified;
import kr.oyez.member.domain.QMember;
import kr.oyez.member.domain.QMemberCertified;

public class MemberCertifiedRepositoryImpl implements MemberCertifiedRepositoryCustom {
	
	private final EntityManager em;
	private final JPAQueryFactory queryFactory;
	
	public MemberCertifiedRepositoryImpl(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	QMember qmember = QMember.member;
	QMemberCertified certified = QMemberCertified.memberCertified;
	
	/*
	@Override
	@Transactional
	public void updateFailLogin(LockMemberDto lockMember) {
		
		em.createNativeQuery("update Member set fail_cnt = ?, lock_yn = ?, lock_date = ? where member_Id = ?")
			.setParameter(1, lockMember.getFailCnt())
			.setParameter(2, lockMember.getLockYn())
			.setParameter(3, lockMember.getLockDate())
			.setParameter(4, lockMember.getMemberId())
			.executeUpdate();
		
		em.close();
	}
	*/
	
	@Override
	public MemberCertified selectCertifiedMember(String memberId) {
		//SELECT * FROM toy.T_PERSON_CERTIFIED WHERE CERTIFIED_YN = 'Y' AND USER_ID = #{ userId }
		
		return queryFactory
				.selectFrom(certified)
				.where(certified.memberId.eq(memberId))
				.fetchOne();
	}

	@Override
	@Transactional
	public void successCertifiedEmail(MemberCertified memberCertified) {
		// UPDATE T_PERSON_CERTIFIED, U_DATE = NOW() WHERE  1=1 AND    USER_ID = #{ userId }
		
		queryFactory
			.update(certified)
			.set(certified.certifiedYn, memberCertified.getCertifiedYn())
			.set(certified.UpdtDate, memberCertified.getUpdtDate())
			.where(certified.memberId.eq(memberCertified.getMemberId()))
			.execute();
	}

	@Override
	@Transactional
	public void successCertifiedRole(Member member) {
		// UPDATE T_PERSON SET    ROLE = 'USER', U_DATE = NOW() WHERE  1=1 AND    USER_ID = #{ userId }
		
		queryFactory
			.update(qmember)
			.set(qmember.role, member.getRole())
			.set(qmember.updtDate, member.getUpdtDate())
			.where(qmember.memberId.eq(member.getMemberId()))
			.execute();
	}
}
