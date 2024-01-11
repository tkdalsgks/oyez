package kr.oyez.member.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kr.oyez.member.domain.Member;
import kr.oyez.member.domain.QMember;
import kr.oyez.member.domain.QMemberCertified;
import kr.oyez.member.dto.LockMemberDto;
import kr.oyez.member.dto.UpdateMemberDto;

public class MemberRepositoryImpl implements MemberRepositoryCustom {
	
	private final EntityManager em;
	private final JPAQueryFactory queryFactory;
	
	public MemberRepositoryImpl(EntityManager em) {
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
	@Transactional
	public void updateFailLogin(LockMemberDto lockMember) {
		
		queryFactory
			.update(qmember)
			.set(qmember.failCnt, lockMember.getFailCnt())
			.set(qmember.lockYn, lockMember.getLockYn())
			.set(qmember.lockDate, lockMember.getLockDate())
			.where(qmember.memberId.eq(lockMember.getMemberId()))
			.execute();
	}

	@Override
	@Transactional
	public long updateProfile(UpdateMemberDto updateMember) {
		//UPDATE T_PERSON SET    USER_NICKNAME = #{ userNickname }, USER_EMAIL = #{ userEmail }, PWD_U_DATE = NOW() WHERE  1=1 AND    USER_ID = #{ userId } AND    USE_YN = 'Y'
		return queryFactory
				.update(qmember)
				.set(qmember.memberNickname, updateMember.getMemberNickname())
				.set(qmember.memberEmail, updateMember.getMemberEmail())
				.set(qmember.updtUser, updateMember.getMemberId())
				.set(qmember.profileUpdtDate, updateMember.getProfileUpdtDate())
				.where(qmember.memberId.eq(updateMember.getMemberId()))
				.execute();
	}

	@Override
	public void updateMemberPwd(Member member) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional
	public void updateProfileImg(Member member) {
		//UPDATE T_PERSON SET    U_DATE = NOW(), PROFILE_IMG = #{ fileUrl } WHERE  1=1 AND    USER_ID = #{ userId }
		
		queryFactory
			.update(qmember)
			.set(qmember.profileImg, member.getProfileImg())
			.set(qmember.updtUser, member.getUpdtUser())
			.set(qmember.updtDate, member.getUpdtDate())
			.where(qmember.memberId.eq(member.getMemberId()))
			.execute();
	}
}
