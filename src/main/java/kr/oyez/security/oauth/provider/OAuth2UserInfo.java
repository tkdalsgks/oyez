package kr.oyez.security.oauth.provider;

public interface OAuth2UserInfo {

	String getMemberId();
	String getMemberEmail();
	String getMemberNickname();
	String getProvider();
}
