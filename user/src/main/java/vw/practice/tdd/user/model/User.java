package vw.practice.tdd.user.model;

import java.util.List;
import java.util.Objects;

import vw.practice.tdd.user.IdGenerator;

public class User {
	
	private int id;
	private String name;
	private List<String> phoneNumber;
	private List<String> address ;
	private String email;
	
	public User(String name, List<String> phoneNumber, List<String> address, String email) {
		super();
		this.id=IdGenerator.generateId();
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(List<String> phonNumber) {
		this.phoneNumber = phonNumber;
	}

	public List<String> getAddress() {
		return address;
	}

	public void setAddress(List<String> address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", phoneNumber=" + phoneNumber + ", address=" + address + ", email="
				+ email + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return id == other.id;
	}
	
	
	
	

}
