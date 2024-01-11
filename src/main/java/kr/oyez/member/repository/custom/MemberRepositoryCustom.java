package kr.oyez.member.repository.custom;

import org.springframework.stereotype.Repository;

import kr.oyez.member.domain.Member;
import kr.oyez.member.dto.LockMemberDto;
import kr.oyez.member.dto.UpdateMemberDto;

@Repository
public interface MemberRepositoryCustom {

	public void updateFailLogin(LockMemberDto params);
	
	public long updateProfile(UpdateMemberDto updateMember);
	
	public void updateMemberPwd(Member member);
	
	public void updateProfileImg(Member member);
}
