package kr.oyez.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LockMemberDto {

	private String memberId;
	private int failCnt;
	private String lockYn;
	private String lockDate;
}
