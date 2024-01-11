package kr.oyez.security.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

	private Map<String, Object> attributes;
	private Map<String, Object> attributesAccount;
	private Map<String, Object> attributesProfile;
	
	@SuppressWarnings("unchecked")
	public KakaoUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
		this.attributesAccount = (Map<String, Object>)attributes.get("kakao_account");
		this.attributesProfile = (Map<String, Object>)attributes.get("properties");
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getMemberId() {
		return String.valueOf(attributes.get("id"));
	}

	@Override
	public String getMemberEmail() {
		return (String) attributesAccount.get("email");
	}

	@Override
	public String getMemberNickname() {
		return (String) attributesProfile.get("nickname");
	}
	
}
