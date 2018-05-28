package com.github.bogdanovmn.boardgameorder.web.app.admin.pricelist;

class FormattedStringBuilder {
	private final StringBuilder stringBuilder;

	FormattedStringBuilder(final StringBuilder stringBuilder) {
		this.stringBuilder = stringBuilder;
	}

	FormattedStringBuilder() {
		this(new StringBuilder());
	}

	FormattedStringBuilder append(String msgFormat, Object... args) {
		stringBuilder.append(
			String.format(
				msgFormat, args
			)
		);
		return this;
	}

	@Override
	public String toString() {
		return this.stringBuilder.toString();
	}
}
