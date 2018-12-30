package servlet.model;

import java.util.*;

public class User {

	private String name;
	private String email;
	private String password;
	private String image;
	private int id;

	public User(
                String name,
                String email,
                String password) {
            this.name = name;
            this.email = email;
            this.password = password;
	}
	public User(
                String name,
                String email,
                String password,
                int id) {
            this(name, email, password);
            this.id = id;
	}
	
	public User(
                String name,
                String email,
                String password,
                int id,
                String image) {
            this(name, email, password, id);
            this.image = image;
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

	public int id() {
            return id;
	}

	public String image() {
            return image;
	}

	@Override
	public String toString() {
            return name + " ][ " + email + " ][ " + password;
	}

}
