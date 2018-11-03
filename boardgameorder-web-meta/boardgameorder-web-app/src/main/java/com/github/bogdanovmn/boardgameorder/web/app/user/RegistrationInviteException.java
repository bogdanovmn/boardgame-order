package com.github.bogdanovmn.boardgameorder.web.app.user;

class RegistrationInviteException extends RegistrationException {
	RegistrationInviteException(String msg) {
		super("inviteCode", msg);
	}
}
