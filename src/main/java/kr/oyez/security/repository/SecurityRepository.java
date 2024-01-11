package kr.oyez.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.oyez.member.domain.Member;
import kr.oyez.security.repository.custom.SecurityRepositoryCustom;

@Repository
public interface SecurityRepository extends JpaRepository<Member, Long>, SecurityRepositoryCustom {

	/**
	 * 회원 조회
	 */
	Member findByMemberId(String memberId);
	Member findByMemberEmail(String memberEmail);
	Member findByMemberNickname(String memberNickname);
}
