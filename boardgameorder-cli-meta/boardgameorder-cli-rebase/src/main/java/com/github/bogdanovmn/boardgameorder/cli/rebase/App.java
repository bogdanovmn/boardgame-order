package com.github.bogdanovmn.boardgameorder.cli.rebase;


import com.github.bogdanovmn.cmdlineapp.CmdLineAppBuilder;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.github.bogdanovmn.boardgameorder.web.orm")
@EntityScan(basePackages = "com.github.bogdanovmn.boardgameorder.web.orm")
@ComponentScan(basePackages = "com.github.bogdanovmn.boardgameorder")
@EnableTransactionManagement
public class App implements CommandLineRunner {
	private final RebaseService rebaseService;

	public App(RebaseService rebaseService) {
		this.rebaseService = rebaseService;
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(App.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	@Override
	public void run(String... args)
		throws Exception
	{
		new CmdLineAppBuilder(args)
			.withJarName("rebase")
			.withDescription("Import list of price lists")
			.withArg("source", "directory with price list files (name format:  price-list--YYYY-MM-DD.xls)")
			.withFlag("without-cleanup", "disable data cleanup before rebase")
			.withEntryPoint(
				cmdLine -> {
					rebaseService.rebase(
						cmdLine.getOptionValue("source"),
						!cmdLine.hasOption("without-cleanup")
					);
				}
			).build().run();
	}
}
