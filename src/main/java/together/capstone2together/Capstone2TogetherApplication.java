package together.capstone2together;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class Capstone2TogetherApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Capstone2TogetherApplication.class, args);

	}



}


