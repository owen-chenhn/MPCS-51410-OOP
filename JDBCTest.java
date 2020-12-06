import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCTest {
	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String host = "localhost";		// DBMS hostname
			String port = "3306";			// port number
			String database = "regie";		// Database to use
			String connectionUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";	// "jdbc:mysql://localhost:3306/regie?useSSL=false"
			String connectionUser = "root";
			String connectionPassword = "------";
			conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM student");
			while (rs.next()) {
				String stuName = rs.getString("student_name");
                String userId = rs.getString("userId");
                String department = rs.getString("department");
				System.out.println("student name: " + stuName + ", userId: " + userId + ", Department Name: " + department);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
	}
}