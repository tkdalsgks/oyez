package kr.oyez.security.oauth.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import kr.oyez.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomUserDetails implements UserDetails, OAuth2User {

	private Member member;
	private Map<String, Object> attributes;
	
	public CustomUserDetails(Member member) {
		this.member = member;
	}
	
	public CustomUserDetails(Member member, Map<String, Object> attributes) {
		this.member = member;
		this.attributes = attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return member.getRoleKey();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return member.getMemberPwd();
	}

	@Override
	public String getUsername() {
		return member.getMemberId();
	}

	/**
	 * 계정 만료 여부 리턴
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 계정 잠김 여부 리턴
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 비밀번호 만료 여부 리턴
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 계정 활성화 여부 리턴
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	@Override
	public String getName() {
		return null;
	}
}
