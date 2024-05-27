package vw.practice.tdd.user;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.Matchers.hasItem;

import vw.practice.tdd.user.controller.UserController;
import vw.practice.tdd.user.model.User;

@SpringBootTest
class UserApplicationTests {
	
	MockMvc mockMvc;
	UserController userController;
	List<User> userList;
	
	@BeforeEach
	void setup() {
		userList = new ArrayList<>();
		User u1 = new User("manu chahar",List.of("9599142687","8130375904"),List.of("Ghaziabad","Delhi"),"m.chahar2687@gmail.com");
		User u2 = new User("mansi agarwal",List.of("8130375904"),List.of("Allahabad"),"mansiagarwal123@gmail.com");
		User u3 = new User("manav tyagi",List.of("8599142687","9123456789"),List.of("Delhi"),"manav234tyagi@gmail.com");
		User u4 = new User("ketan agarwal",List.of("9599142687"),List.of("Delhi","Bangalore"),"ka456@gmail.com");
		userList.add(u1);
		userList.add(u2);
		userList.add(u3);
		userList.add(u4);
		
		userController = new UserController(userList);
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
		
	}

	@Test
	void shoudReturnAllUsersIfListNotEmpty() throws Exception {
		mockMvc.perform(get("/users"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$",hasSize(5)));
	}
	
	@Test
	void shouldReturnTrueWhenListIsEmpty() throws Exception {
		userList.clear();
		mockMvc.perform(get("/users"))
		.andExpect(jsonPath("$",hasSize(0)));
		
		assertTrue(userList.isEmpty());
	}
	
	@Test
	void shouldReturnUserWhenIdIsGiven() throws Exception {
		int uId=userList.get(0).getId();
		mockMvc.perform(get("/user/{id}",uId))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name").value("manu chahar"))
		.andExpect(jsonPath("$.phoneNumber",hasItem("9599142687")))
		.andExpect(jsonPath("$.phoneNumber",hasItem("8130375904")))
		.andExpect(jsonPath("$.address",hasItem("Ghaziabad")))
		.andExpect(jsonPath("$.address",hasItem("Delhi")))
		.andExpect(jsonPath("$.email").value("m.chahar2687@gmail.com"));
	}
	
	@Test
	void shouldReturnNullWhenIdDoesNotExists() throws Exception {
		int uId=876;

		mockMvc.perform(get("/user/{id}",uId));
		assertNull(userController.getUserById(uId));
	}
	
	@Test
	void shouldReturnUsersHavingMultipleData() throws Exception {
		mockMvc.perform(get("/multipleDataUser"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$",hasSize(1)));
	}
	
	@Test
	void shouldReturnEmptyListIfNoUserHaveMultipleData() throws Exception {
		userList.remove(0);
		mockMvc.perform(get("/multipleDataUser"))
		.andExpect(jsonPath("$",hasSize(0)));
		assertEquals(new ArrayList<>(),userController.getUsersWithMultiplePhonenumberAddress());
	}
	
	@Test
	void shouldCreateUserWhenDataGiven() throws Exception {
		String newUser = "{ \"name\":\"sonia tomer\", \"phoneNumber\":[\"8599142687\",\"7130375904\"], \"address\":[\"Gurgaon\"] ,\"email\":\"sona56@gmail.com\" }";
		mockMvc.perform(post("/createuser").contentType(MediaType.APPLICATION_JSON).content(newUser))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.name").value("sonia tomer"))
		.andExpect(jsonPath("$.phoneNumber",hasItem("8599142687")))
		.andExpect(jsonPath("$.phoneNumber",hasItem("7130375904")))
		.andExpect(jsonPath("$.address",hasItem("Gurgaon")))
		.andExpect(jsonPath("$.email").value("sona56@gmail.com"));
	}
	
	@Test
	void shouldUpdateUserWhenDataGiven() throws Exception {
		String updatedUser = "{ \"name\":\"mansi agarwal\", \"phoneNumber\":[\"8130375904\"], \"address\":[\"Prayagraj\"] ,\"email\":\"mansiagarwal123@gmail.com\" }";
		int id=userList.get(1).getId();
		mockMvc.perform(put("/updateuser/{id}",id).contentType(MediaType.APPLICATION_JSON).content(updatedUser))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name").value("mansi agarwal"))
		.andExpect(jsonPath("$.phoneNumber",hasItem("8130375904")))
		.andExpect(jsonPath("$.address",hasItem("Prayagraj")))
		.andExpect(jsonPath("$.email").value("mansiagarwal123@gmail.com"));
	}
	
	@Test
	void shouldReturnFalseWhenUserDeleted() throws Exception {
		int id = userList.get(1).getId();
		User user = userList.get(1);
		mockMvc.perform(delete("/deleteuser/{id}",id))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name").value("mansi agarwal"))
		.andExpect(jsonPath("$.phoneNumber",hasItem("8130375904")))
		.andExpect(jsonPath("$.address",hasItem("Allahabad")))
		.andExpect(jsonPath("$.email").value("mansiagarwal123@gmail.com"));
	}
	
	@Test
	void shouldReturnNullIfUserDoesNotExistsWhenDeleting() throws Exception {
		int id=23;
		mockMvc.perform(put("/deleteuser/{id}",id));
		assertEquals(null,userController.deleteUser(id));
	}
	

}
