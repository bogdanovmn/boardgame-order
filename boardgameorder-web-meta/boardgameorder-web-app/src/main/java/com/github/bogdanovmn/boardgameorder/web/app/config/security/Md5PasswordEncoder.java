package com.github.bogdanovmn.boardgameorder.web.app.config.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

public class Md5PasswordEncoder implements PasswordEncoder {
	@Override
	public String encode(CharSequence rawPassword) {
		return rawPassword == null
			? null
			: DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encode(rawPassword).equals(encodedPassword);
	}

}