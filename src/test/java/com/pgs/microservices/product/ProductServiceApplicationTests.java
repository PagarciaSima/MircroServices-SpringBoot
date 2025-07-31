package com.pgs.microservices.product;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.MongoDBContainer;

import io.restassured.RestAssured;

@Import(TestcontainersConfiguration.class)

// Asign a random port to avoid conflicts
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {
	
	// Automatically determines the connection data for the container
	@ServiceConnection
	// Reference to img name from docker compose 'mongo:7.0.5'
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");
	
	// Dynamically checks during runtime which random port was assigned for the test
	@LocalServerPort
	private Integer port;
	
	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = this.port;
	}
	
	// Static block to start the MongoDB container once when the class is loaded
	static {
		mongoDBContainer.start();
	}

	@Test
	void shouldCreateProduct() {
		String requestBody = """			
		{

		    "name": "IPhone 11",
		    "description": "IPhone 11 is a smartphone from Apple",
		    "price": 1500
		}
		""";
		
		RestAssured.given()
			.contentType("application/json")
			.body(requestBody)
			.when()
			.post("/api/product")
			.then()
			.statusCode(HttpStatus.CREATED.value())
			.body("id", Matchers.notNullValue())
			.body("name", Matchers.equalTo("IPhone 11"))
			.body("description", Matchers.equalTo("IPhone 11 is a smartphone from Apple"))
			.body("price", Matchers.equalTo(1500));
	}

}
