package Pruetec.util;

public class Seeder {

	public Seeder() {
		String databasePath = "src/main/resources/database.db";
		String url = "jdbc:sqlite:" + databasePath;
		Connection conn = DriverManager.getConnection(url);
		
	}
	
}
