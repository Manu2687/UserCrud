package vw.practice.tdd.user.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import vw.practice.tdd.user.model.User;

@RestController
public class UserController {
	
	List<User> userList;
	
	User u4 = new User("ketan agarwal",List.of("9599142687"),List.of("Delhi","Bangalore"),"ka456@gmail.com");
	
	public UserController(List<User> userList) {
		this.userList=userList;
		userList.add(u4);
	}
	
	@GetMapping("/users")
	public List<User> getAllUsers() {
		userList.forEach(System.out::println);
		return userList;
	}
	
	@GetMapping("/user/{id}")
	public User getUserById(@PathVariable int id) {
		return userList.stream().filter(u -> u.getId()==id)
				.findFirst()
				.orElse(null);
	}
	
	@GetMapping("/multipleDataUser")
	public List<User> getUsersWithMultiplePhonenumberAddress() {
		return userList.stream()
				.filter(u->u.getPhoneNumber().size()>1)
				.filter(u->u.getAddress().size()>1)
				.collect(Collectors.toList());
	}
	
	@PostMapping("/createuser")
	public User createUser(@RequestBody User user) {
		userList.add(user);
		return user;
	}
	
	@PutMapping("/updateuser/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User user) {
		for(User u: userList) {
			if(u.getId()==id) {
				u.setName(user.getName());
				u.setPhoneNumber(user.getPhoneNumber());
				u.setAddress(user.getAddress());
				u.setEmail(user.getEmail());
				break;
			} 
		}
		return user;
	}
	
	@DeleteMapping("/deleteuser/{id}")
	public User deleteUser(@PathVariable int id) {
		for(User u: userList) {
			if(u.getId()==id) {
				System.out.println(u+"\n"+id);
				userList.remove(u);				
				if(!userList.contains(u)) {
					return u;
				}
			}
		}
		return null;
	}
	

}
