package com.daikit.graphql.spring.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point
 *
 * @author Thibaut Caselli
 */
@SpringBootApplication
public class ApplicationStarter implements CommandLineRunner {

	/**
	 * Entry point
	 *
	 * @param args
	 *            arguments
	 */
	public static void main(final String[] args) {
		new SpringApplication(ApplicationStarter.class).run(args);
	}

	@Override
	public void run(final String... args) throws Exception {
		if (args.length > 0 && args[0].equals("exitcode")) {
			throw new ExitException();
		}
	}

	class ExitException extends RuntimeException implements ExitCodeGenerator {
		private static final long serialVersionUID = 1L;

		@Override
		public int getExitCode() {
			return 10;
		}
	}

}
