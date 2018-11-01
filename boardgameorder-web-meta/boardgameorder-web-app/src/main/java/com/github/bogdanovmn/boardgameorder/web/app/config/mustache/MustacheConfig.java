package com.github.bogdanovmn.boardgameorder.web.app.config.mustache;

import com.samskivert.mustache.Mustache;
import org.springframework.boot.autoconfigure.mustache.MustacheEnvironmentCollector;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

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
			.withFormatter(obj -> {
				if (obj instanceof Date) {
					return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(obj);
				}
				else {
					return obj.toString();
				}
			})
			.defaultValue("")
			.withLoader(mustacheTemplateLoader)
			.withCollector(collector);

	}
}
