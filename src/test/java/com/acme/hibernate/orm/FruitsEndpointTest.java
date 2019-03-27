package com.acme.hibernate.orm;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FruitsEndpointTest {

	@Autowired
	private MockMvc client;

	@Test
	public void testListAllFruits() throws Exception {
		// List all, should have all 3 fruits the database has initially:
		client.perform(get("/fruits")).andExpect(status().isOk())
				.andExpect(content().string(containsString("Cherry")));
	}

}
