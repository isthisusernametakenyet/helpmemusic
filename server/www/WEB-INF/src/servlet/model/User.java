package servlet.model;

import java.util.*;

public class User {

	private String name;
	private String email;
	private String password;
	private List<User> friends;

	public User(
			String name,
			String email,
                        String password,
			List<User> friends) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.friends = friends;
	}

	public boolean addFriend(User user) {
		return friends.add(user);
	}

	public boolean removeFriend(User user) {
		return friends.removeIf(friend -> friend.name().equals(user.name()));
	}

	public String name() {
		return name;
	}

	public String password() {
		return password;
	}

	public String email() {
		return email;
	}

	public List<User> friends() {
		return Collections.unmodifiableList(friends);
	}

	@Override
	public String toString() {
		return name + " ][ " + email + " ][ " + password;
	}

}
