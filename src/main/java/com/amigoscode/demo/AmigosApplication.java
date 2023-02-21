package com.amigoscode.demo;

import com.amigoscode.demo.collections.Address;
import com.amigoscode.demo.collections.Gender;
import com.amigoscode.demo.collections.Student;
import com.amigoscode.demo.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@SpringBootApplication
public class AmigosApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmigosApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(
			StudentRepository repository, MongoTemplate mongoTemplate){
		return args -> {
			Address address = new Address(
					"usa",
					"sd",
					"98444"
			);
			String email = "rreerer@gmail.com";
			Student student = new Student(
					"Eric",
					"lopp",
					email,
					Gender.FEMALE,
					address,
					List.of("computer science", "mathss"),
					BigDecimal.TEN,
					LocalDateTime.now()
			);

			usingMongoTemplateAndQuery(repository, mongoTemplate, email, student);

			repository.findStudentByEmail(email)
					.ifPresentOrElse(s -> {
						System.out.println(student + " already exists");
					}, ()-> {
						System.out.println("inserting students" + student);
						repository.insert(student);
					});
		};
	}

	private void usingMongoTemplateAndQuery(StudentRepository repository, MongoTemplate mongoTemplate, String email, Student student) {
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(email));

		List<Student> students = mongoTemplate.find(query, Student.class);

		if(students.size() > 1) {
			throw new IllegalStateException("found many students with email" + email);
		}

		if(students.isEmpty()) {
			System.out.println("inserting students" + student);
			repository.insert(student);
		}
		else {
			System.out.println(student + " already exists");
		}
	}

}
