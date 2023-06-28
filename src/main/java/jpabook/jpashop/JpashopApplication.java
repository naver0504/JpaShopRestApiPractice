package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

	/***
	 *
	 * Entity를 반환하는 것은 좋지 않음
	 * 이런 방법이 있는 것만 알자
	 */
	@Bean
	public Hibernate5JakartaModule hibernate5Module() {
		return new Hibernate5JakartaModule();
	}


}
