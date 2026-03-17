package com.project.api;

import com.project.api.support.TestSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestSecurityConfiguration.class)
class ApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
