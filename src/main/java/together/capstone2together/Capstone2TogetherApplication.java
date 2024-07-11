package together.capstone2together;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
public class Capstone2TogetherApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Capstone2TogetherApplication.class, args);

	}



}


