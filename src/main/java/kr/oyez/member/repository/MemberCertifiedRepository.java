package kr.oyez.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.oyez.member.domain.MemberCertified;
import kr.oyez.member.repository.custom.MemberCertifiedRepositoryCustom;

@Repository
public interface MemberCertifiedRepository extends JpaRepository<MemberCertified, Long>, MemberCertifiedRepositoryCustom {
	
}
