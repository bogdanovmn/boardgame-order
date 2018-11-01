package com.github.bogdanovmn.boardgameorder.web.app.user;

class RegistrationException extends Exception {
	private final String field;
	private final String msg;

	RegistrationException(String field, String msg) {
		this.field = field;
		this.msg = msg;
	}

	RegistrationException(String msg) {
		this.field = null;
		this.msg = msg;
	}


	String getField() {
		return field;
	}

	String getMsg() {
		return msg;
	}

	boolean isCustomError() {
		return field == null;
	}
}
