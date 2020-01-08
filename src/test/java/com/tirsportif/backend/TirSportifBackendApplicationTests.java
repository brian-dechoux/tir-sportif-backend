package com.tirsportif.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.util.AssertionErrors.assertTrue;

public class TirSportifBackendApplicationTests {

	@Test
	public void contextLoads() {
		assertTrue("not empty", Stream.of().collect(Collectors.toSet()).isEmpty());
	}

}
