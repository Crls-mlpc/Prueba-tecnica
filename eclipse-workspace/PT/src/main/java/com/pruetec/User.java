package com.pruetec;

import java.util.List;
import java.util.UUID;

public class User {
	public UUID id;
	public String email;
	public String name;
	public String phone;
	public String password;
	public String taxId;
	public String createdAt;
	public List <Address> addresses;

}
