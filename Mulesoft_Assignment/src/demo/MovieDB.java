package demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MovieDB {
	
	// Method to delete 'Movies' table
	private static void deleteTable(Connection conn) throws SQLException {
		String deleteTableQuery ="DROP TABLE Movies";
		Statement deleteTableStmt = conn.createStatement();
		deleteTableStmt.execute(deleteTableQuery);		
	}
	
	// Method to insert rows into the database
	private static void insertMovie(Connection conn, String name, String actor, String actress, String director, int year) throws SQLException {
			
			String insertQuery ="INSERT INTO Movies(name,actor,actress,director,year) VALUES(?,?,?,?,?)";
			PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
			insertStmt.setString(1, name);
			insertStmt.setString(2, actor);
			insertStmt.setString(3, actress);
			insertStmt.setString(4, director);
			insertStmt.setInt(5, year);
			insertStmt.executeUpdate();	
	}
	
	// Method to display all entries from database table
	private static void displayDB(Connection conn, String tableName) throws SQLException {
		
			String selectQuery ="SELECT * from " + tableName;
			Statement stmt = conn.createStatement();
			ResultSet selectResult = stmt.executeQuery(selectQuery);
			
			while(selectResult.next()) {
				
				String name = selectResult.getString("name");
				String actor = selectResult.getString("actor");
				String actress = selectResult.getString("actress");
				String director = selectResult.getString("director");
				String year = selectResult.getString("year");
				
				System.out.println(name+"\t"+actor+"\t"+actress+"\t"+director+"\t"+year);
			}					
	}
	
	// Method to display movies by filtering according to actor name
	private static void displayFilterActor(Connection conn, String tableName, String actor) throws SQLException {
		String selectSQL ="SELECT name from " + tableName + " WHERE actor = '" + actor +"'";
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(selectSQL);
		System.out.println("Movies by actor " + actor);
		while(rs.next()) {
			System.out.println(rs.getString("name"));
		}
	}
	
	// Method to display movies by filtering according to director name
		private static void displayFilterDirector(Connection conn, String tableName, String director) throws SQLException {
			String selectSQL ="SELECT name from " + tableName + " WHERE director = '" + director +"'";
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery(selectSQL);
			System.out.println("Movies by director " + director);
			while(rs.next()) {
				System.out.println(rs.getString("name"));
			}
		}

	// Main method
	public static void main(String[] args) {
		
		Connection conn  = null;
		
		try {
			conn=DriverManager.getConnection("jdbc:sqlite:C:/sqlite/db/moviedb.db");
			System.out.println("Database connection successful\n");
			try {
				deleteTable(conn);
			}
			catch(Exception e)
			{
				System.out.println("Exception occurred : " + e);
			}
			
			// Creating 'Movies' table
			String createTablesql ="CREATE TABLE IF NOT EXISTS Movies (name text, actor text NOT NULL, actress text NOT NULL, director text NOT NULL, year integer);";  
			Statement stmt=conn.createStatement();
			stmt.execute(createTablesql);
			
			// Inserting values into 'Movies' table			
			insertMovie (conn,"Mersal","Vijay","Samantha","Atlee",2017);
			insertMovie (conn,"PK","Aamir Khan","Anushka Sharma","Rajkumar Hirani",2014);
			insertMovie (conn,"Pokiri","Mahesh Babu","Ileana","Puri Jagannadh",2005);
			insertMovie (conn,"Drishyam","Mohanlal","Meena","Jeethu Joseph",2013);
			insertMovie (conn,"Thuppakki","Vijay","Kajal","Murugadoss",2012);
			insertMovie (conn,"Sivaji","Rajnikanth","Shreya","Shankar",2007);
			insertMovie (conn,"Dangal","Aamir Khan","Fatima","Nitesh Tiwari",2016);

			// Displaying entire table
			displayDB(conn,"Movies");
			
			System.out.println();
			// Filtering movie according to actor
			displayFilterActor(conn,"Movies","Vijay");
			
			System.out.println();			
			displayFilterDirector(conn,"Movies","Shankar");
			
		} catch (SQLException e) { 
			
			System.out.println("Exception occurred : " + e);
			
		} finally {
			
			if(conn!=null) {
				try {
					conn.close();
				}
				catch(SQLException e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
			}
		}
	}	
}