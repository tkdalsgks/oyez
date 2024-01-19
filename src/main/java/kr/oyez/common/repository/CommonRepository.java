package kr.oyez.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.oyez.common.domain.Common;
import kr.oyez.common.repository.custom.CommonRepositoryCustom;

@Repository
public interface CommonRepository extends JpaRepository<Common, Long>, CommonRepositoryCustom {

	
}
