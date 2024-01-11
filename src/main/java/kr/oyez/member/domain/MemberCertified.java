package kr.oyez.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCertified {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "certified_no")
	private Long id;
	
	private String memberId;
	
	private String certifiedCode;
	
	private String certifiedYn;
	
	private String regDate;
	
	private String UpdtDate;

	public MemberCertified enableEmail(String memberId, String updtDate) {
		return MemberCertified.builder()
				.memberId(memberId)
				.UpdtDate(updtDate)
				.build();
	}
}
