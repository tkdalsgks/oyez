package kr.oyez.security.oauth.config;

import java.util.Map;

import kr.oyez.member.domain.Member;
import kr.oyez.member.domain.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {

	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String memberId;
	private String memberEmail;
	private String memberNickname;
	private String provider;
	private Role role;
	
	@Builder
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String memberId, String memberNickname, String provider, String memberEmail, Role role) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.memberId = memberId;
		this.memberNickname = memberNickname;
		this.memberEmail = memberEmail;
		this.provider = provider;
		this.role = role;
	}
	
	public static OAuthAttributes of(String registrationId, String memberNameAttributeName, Map<String, Object> attributes) {
		switch(registrationId) {
		case "kakao":
			return ofKakao(memberNameAttributeName, attributes);
		case "google":
			return ofGoogle(memberNameAttributeName, attributes);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static OAuthAttributes ofKakao(String memberNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
		
		String kakaoEmail = (String) kakaoAccount.get("email");
		String[] kakaoId = kakaoEmail.split("@");
		String memberId = kakaoId[0];
		
		return OAuthAttributes.builder()
				.memberId(memberId)
				.memberNickname((String) kakaoProfile.get("nickname"))
				.memberEmail((String) kakaoAccount.get("email"))
				.provider("kakao")
				.attributes(attributes)
				.nameAttributeKey(memberNameAttributeName)
				.build();
	}
	
	private static OAuthAttributes ofGoogle(String memberNameAttributeName, Map<String, Object> attributes) {
		String googleEmail = (String) attributes.get("email");
		String[] googleId = googleEmail.split("@");
		String memberId = googleId[0];
		
		return OAuthAttributes.builder()
				.memberId(memberId)
				.memberNickname((String) attributes.get("name"))
				.memberEmail((String) attributes.get("email"))
				.provider("google")
				.attributes(attributes)
				.nameAttributeKey(memberNameAttributeName)
				.build();
	}
	
	public Member Entity() {
		return Member.builder()
				.memberId(memberId)
				.memberNickname(memberNickname)
				.memberEmail(memberEmail)
				.provider(provider)
				.role(Role.GUEST)
				.build();
	}
}
