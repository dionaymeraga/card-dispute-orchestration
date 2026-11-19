package io.github.dionaymeraga.disputeworkers;

import io.camunda.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Deployment(resources = {
		"classpath:card-dispute.bpmn",
		"classpath:dispute-route.dmn",
		"classpath:review-dispute.form"
})
public class DisputeWorkersApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisputeWorkersApplication.class, args);
	}

}