package com.github.bogdanovmn.boardgameorder.web.app.config.mustache;

import com.samskivert.mustache.Mustache;
import org.springframework.boot.autoconfigure.mustache.MustacheEnvironmentCollector;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MustacheConfig {
	@Bean
	public Mustache.Compiler mustacheCompiler(
		Mustache.TemplateLoader mustacheTemplateLoader,
		Environment environment
	) {

		MustacheEnvironmentCollector collector = new MustacheEnvironmentCollector();
		collector.setEnvironment(environment);

		return Mustache.compiler()
			.defaultValue("")
			.withLoader(mustacheTemplateLoader)
			.withCollector(collector);

	}
}
