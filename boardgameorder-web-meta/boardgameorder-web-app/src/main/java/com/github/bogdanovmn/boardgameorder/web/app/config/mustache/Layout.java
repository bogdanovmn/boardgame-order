package com.github.bogdanovmn.boardgameorder.web.app.config.mustache;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.IOException;
import java.io.Writer;

public class Layout implements Mustache.Lambda {
	private String body;
	private final Mustache.Compiler compiler;
	private final String name;

	public Layout(Mustache.Compiler compiler, String name) {
		this.compiler = compiler;
		this.name = name;
	}

	public Layout(Mustache.Compiler compiler) {
		this.compiler = compiler;
		this.name = "min";
	}

	@Override
	public void execute(Template.Fragment frag, Writer out) throws IOException {
		body = frag.execute();
		compiler.compile(
			String.format("{{>layout_%s}}", this.name)
		).execute(frag.context(), out);
	}

	public String getBody() {
		return body;
	}

}