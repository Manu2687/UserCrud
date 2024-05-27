package vw.practice.tdd.user;

public class IdGenerator {
	
	static int id=1000;
	
	public static int generateId() {
		return id++;
	}

}
