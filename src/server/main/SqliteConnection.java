package server.main;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqliteConnection {
	public static Connection Connector(){

		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("user.dir") + "/ScheduleDB");

			System.out.println("Database path is "+System.getProperty("user.dir"));
			return conn;
		} catch (Exception e) {

			System.out.println("Unhandled exception from SqliteConnection");
			return null;
		}


	}
}