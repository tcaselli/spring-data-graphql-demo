package com.daikit.graphql.spring.demo.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.daikit.graphql.spring.demo.ApplicationStarter;

/**
 * Tester for application startup
 *
 * @author Thibaut Caselli
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationStarter.class)
public class SpringContextTest {

	/**
	 * Test application can start
	 */
	@Test
	public void whenSpringContextIsBootstrapped_thenNoExceptions() {
		// Nothing done
	}

}
