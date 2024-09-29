package br.edu.fatecourinhos.thread;

import br.edu.fatecourinhos.thread.sample.SampleCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ThreadDemoApplication implements CommandLineRunner, ExitCodeGenerator {

	@Autowired
	private ApplicationContext context;
	private int exitCode = 0;

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(ThreadDemoApplication.class, args)));
	}

	@Override
	public void run(String... args) throws Exception {
		var p = context.getBean(SampleCommand.class);
		exitCode = p.run();
	}

	@Override
	public int getExitCode() {
		return exitCode;
	}
}
