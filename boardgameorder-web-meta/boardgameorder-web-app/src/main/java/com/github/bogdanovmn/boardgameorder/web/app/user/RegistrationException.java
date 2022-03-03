package com.github.bogdanovmn.boardgameorder.web.app.user;

class RegistrationException extends Exception {
    private final String field;
    private final String msg;

    RegistrationException(String field, String msg) {
        super(msg);
        this.field = field;
        this.msg = msg;
    }

    RegistrationException(String msg) {
        this(null, msg);
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
