package com.pruetec;

import java.sql.*;
import java.util.*;

public class Database {
	
	private static final String URL = "jdbc:sqlite:database.db";
	
	
	static {
		try (Connection conn = DriverManager.getConnection(URL)){
			try (Statement stmt = conn.createStatement()){
				stmt.execute("""
						CREATE TABLE IF NOT EXISTS users (
						
						id TEXT PRIMARY KEY,
						email TEXT,
						name TEXT,
						phone TEXT,
						password TEXT,
						tax_id TEXT UNIQUE,
						created_at TEXT
						)
						""");
				
				stmt.execute("""
						
						CREATE TABLE IF NOT EXISTS addresses (
						
						id INTEGER PRIMARY KEY AUTOINCREMENT,
						user_id TEXT,
						name TEXT,
						street TEXT,
						country_code TEXT,
						FOREIGN KEY(user_id) REFERENCES users(id)
						)
						""");
			}
			
			try (Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users")){
				
				if (rs.next() && rs.getInt(1) == 0){
					for (int i =1; i <=3; i++) {
						String userId = UUID.randomUUID().toString();
						PreparedStatement psUser = conn.prepareStatement("""
								
								INSERT INTO users (id, email, name, phone, password, tax_id, created_at)
								VALUES (?, ?, ?, ?, ?, ?, ?)
								""");
						
						psUser.setString(1, userId);
						psUser.setString(2, "user" + i + "@mail.com");
						psUser.setString(3, "user" + i);
						psUser.setString(4, "+1 55 555 555 5" + i);
	                    psUser.setString(5, CryptoUtil.encrypt("password" + i));
	                    psUser.setString(6, "AARR990101XX" + i);
	                    psUser.setString(7, TimeUtil.nowMadagascar());
	                    psUser.executeUpdate();
	                    
	                    insertAddress(conn, userId, "workaddress", "street No. " + (i * 2 - 1), "UK");
                        insertAddress(conn, userId, "homeaddress", "street No. " + (i * 2), "AU");
						
						
					}
				}
			}
		}
		
		catch (SQLException e) {
			
			throw new RuntimeException(e);
		}
	}
	
	 public static void insertUser(User u) {
	        try (Connection conn = DriverManager.getConnection(URL)) {
	            try (PreparedStatement ps = conn.prepareStatement("""
	                INSERT INTO users (id, email, name, phone, password, tax_id, created_at)
	                VALUES (?, ?, ?, ?, ?, ?, ?)
	            """)) {
	                ps.setString(1, u.id.toString());
	                ps.setString(2, u.email);
	                ps.setString(3, u.name);
	                ps.setString(4, u.phone);
	                ps.setString(5, u.password);     // ya debe venir cifrada con CryptoUtil.encrypt(...)
	                ps.setString(6, u.taxId);
	                ps.setString(7, u.createdAt);
	                ps.executeUpdate();
	            }

	            if (u.addresses != null) {
	                for (Address a : u.addresses) {
	                    insertAddress(conn, u.id.toString(), a.name, a.street, a.countryCode);
	                }
	            }
	        } catch (SQLException e) {
	            throw new RuntimeException(e);
	        }
	    }
	
	private static void insertAddress(Connection conn, String userId, String name, String street, String country) throws SQLException{
		PreparedStatement ps = conn.prepareStatement("""
				
				INSERT INTO addresses (user_id, name, street, country_code)
				VALUES (?, ?, ?, ?)
				""");
		
		ps.setString(1, userId);
        ps.setString(2, name);
        ps.setString(3, street);
        ps.setString(4, country);
        ps.executeUpdate();
	}
	
	public static List<User> getAllUsers(){
		
		List<User> users = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(URL)){
			
			String sql = "SELECT *FROM users";
			try(Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql)) {
						while (rs.next()) {
							User user = new User();
							
							user.id = UUID.fromString(rs.getString("id"));
		                    user.email = rs.getString("email");
		                    user.name = rs.getString("name");
		                    user.phone = rs.getString("phone");
		                    user.password = rs.getString("password");
		                    user.taxId = rs.getString("tax_id");
		                    user.createdAt = rs.getString("created_at");
		                    user.addresses = getAddressesByUser(conn, user.id.toString());
		                    users.add(user);
		                    
						}
					}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
	
	private static List<Address> getAddressesByUser(Connection conn, String userId) throws SQLException {
        List<Address> addresses = new ArrayList<>();
        
        String sql = "SELECT * FROM addresses WHERE user_id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Address addr = new Address();
                    addr.id = rs.getInt("id");
                    addr.name = rs.getString("name");
                    addr.street = rs.getString("street");
                    addr.countryCode = rs.getString("country_code");
                    addresses.add(addr);
                }
            }
        }
        return addresses;
	}
}
