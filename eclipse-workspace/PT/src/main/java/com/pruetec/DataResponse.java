package com.pruetec;

import java.util.List;
import java.util.UUID;

public class DataResponse {
	
	public UUID id;
    public String email;
    public String name;
    public String phone;
    public String taxId;
    public String createdAt;
    public List<Address> addresses;
    
    public static DataResponse from(User u) {
    	
    	DataResponse r= new DataResponse();
    	
    	r.id = u.id;
    	r.email = u.email;
    	r.name = u.name;
    	r.phone = u.phone;
    	r.taxId = u.taxID;
    	r.createdAt = u.createdAt;
    	r.addresses = u.addresses;
    	
    	return r;
    }

}
