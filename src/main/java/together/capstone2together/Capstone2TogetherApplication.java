package together.capstone2together;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import together.capstone2together.domain.ItemTag;
import together.capstone2together.domain.Member;
import together.capstone2together.domain.MemberTag;
import together.capstone2together.domain.Tag;
import together.capstone2together.service.MemberService;
import together.capstone2together.service.MemberTagService;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class Capstone2TogetherApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Capstone2TogetherApplication.class, args);

	}



}


