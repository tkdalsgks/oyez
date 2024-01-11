package kr.oyez.security.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kr.oyez.common.domain.LoginLog;
import kr.oyez.common.domain.QLoginLog;
import kr.oyez.common.utils.StringUtils;
import kr.oyez.member.domain.MemberCertified;

public class SecurityRepositoryImpl implements SecurityRepositoryCustom {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;
	
	public SecurityRepositoryImpl(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	QLoginLog loginlog = QLoginLog.loginLog;

	@Override
	@Transactional
	public void insertLoginLog(LoginLog loginLog) {
		
		em.createNativeQuery("insert into LOGIN_LOG (LOGIN_ID, ACCESS_IP, LOGIN_DATE) values (?, ?, ?)")
			.setParameter(1, loginLog.getLoginId())
			.setParameter(2, loginLog.getAccessIp())
			.setParameter(3, loginLog.getLoginDate())
			.executeUpdate();
		
		em.close();
	}

	@Override
	public MemberCertified selectCertifiedMember(String memberId) {
		return null;
	}
}
