package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootJpaApplication {

	public static void main(String[] args) {

//		Hello hello = new Hello();
//		hello.setData("Hello");
//		String data = hello.getData();
//		System.out.println("data = " + data);


		SpringApplication.run(SpringBootJpaApplication.class, args);
	}

	@Bean
	Hibernate5JakartaModule hibernate5JakartaModule(){
		Hibernate5JakartaModule hibernate5JakartaModule = new Hibernate5JakartaModule();
		//hibernate5JakartaModule.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING, true);
		return hibernate5JakartaModule;
	}

}
