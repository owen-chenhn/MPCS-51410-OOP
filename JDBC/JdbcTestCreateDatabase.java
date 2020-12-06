import java.sql.*;

public class JdbcTestCreateDatabase {
   public static void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;
   try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String connectionUrl = "jdbc:mysql://127.0.0.1:3306/employees?useSSL=false";
			String connectionUser = "test";
			String connectionPassword = "test";
			conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);

			stmt = conn.createStatement();
			
			String sql = "CREATE DATABASE TESTNEWDB";
			stmt.executeUpdate(sql);
			System.out.println("Database created successfully...");
	} catch( Exception e )
	{
		e.printStackTrace();
	}
	System.out.println("Goodbye!");
	}  
}


