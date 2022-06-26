package com.springboot.webservices.staffscheduling.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.webservices.staffscheduling.controller.UserController;
import com.springboot.webservices.staffscheduling.dao.repository.RoleRepository;
import com.springboot.webservices.staffscheduling.dao.repository.ScheduleRepository;
import com.springboot.webservices.staffscheduling.dao.repository.UserRepository;
import com.springboot.webservices.staffscheduling.payload.LoginDto;
import com.springboot.webservices.staffscheduling.payload.SignUpDto;
import com.springboot.webservices.staffscheduling.security.CustomUserDetailsService;


@WebMvcTest(UserController.class)
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private RoleRepository roleRepository;

	@MockBean
	private ScheduleRepository scheduleRepository;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private CustomUserDetailsService userDetailsService;



	@BeforeEach
	public void setUp() {

		LoginDto loginDto = new LoginDto();
		loginDto.setUsernameOrEmail("admin");
		loginDto.setPassword("admin");

		
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				loginDto.getUsernameOrEmail(), loginDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testGetUsers() throws Exception {

		RequestBuilder request = MockMvcRequestBuilders
				.get("/users")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andReturn();
		assertTrue(result != null);

	}

	@Test
	public void testGetUsersById() throws Exception {


		MvcResult result = null;
		try {
			result = mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}")
					.content("2")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andReturn();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(result);
		assertTrue(result != null);
	}

	@Test
	public void testRegisterUser() throws Exception {
		SignUpDto signUpDto = new SignUpDto();
		signUpDto.setName("thomson");
		signUpDto.setPassword("thomson");
		signUpDto.setEmail("thomson@gmail.com");
		signUpDto.setUsername("thomson");

		MvcResult result = null;
		try {
			result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
					.content(asJsonString(signUpDto))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andReturn();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertNotNull(result);
	}
}


//	assertNotNull(userRest);
//		assertEquals(USER_ID, userRest.getContent().getId());
//		assertEquals(user.getName(), userRest.getContent().getName());
//		assertTrue(user.getEmail().equals(userRest.getContent().getEmail()));