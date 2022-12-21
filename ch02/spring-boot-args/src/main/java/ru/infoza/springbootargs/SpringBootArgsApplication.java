package ru.infoza.springbootargs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.List;

@SpringBootApplication
public class SpringBootArgsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootArgsApplication.class, args);
	}

}

@Component
class MyComponent {
	private static final Logger log = LoggerFactory.getLogger(MyComponent.class);

	@Autowired
	public MyComponent(ApplicationArguments args) {
		boolean enable = args.containsOption("enable");
		if (enable)
			log.info("## > You are enabled!");
		List<String> _args = args.getNonOptionArgs();
		if (!_args.isEmpty()) {
			log.info("## > extra args ...");
			_args.forEach(file -> log.info(file));
		}

	}
}
