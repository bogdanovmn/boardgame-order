package com.github.bogdanovmn.boardgameorder.web.app.config.security;

import com.github.bogdanovmn.boardgameorder.web.orm.User;
import com.github.bogdanovmn.boardgameorder.web.orm.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.stream.Collectors;

public class ProjectUserDetails implements UserDetails {
	private final User user;

	public ProjectUserDetails(User user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.commaSeparatedStringToAuthorityList(
			StringUtils.collectionToCommaDelimitedString(
				this.user.getRoles().stream()
					.map(UserRole::getName)
					.collect(Collectors.toList())
			)
		);
	}

	@Override
	public String getPassword() {
		return this.user.getPasswordHash();
	}

	@Override
	public String getUsername() {
		return this.user.getName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public User getUser() {
		return this.user;
	}
}
