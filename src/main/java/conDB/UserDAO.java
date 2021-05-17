package conDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Bean.LoginBean;

public class UserDAO {

	// Declaring the variables
	static Connection con = null;
	static PreparedStatement stmt = null;
	static ResultSet rs = null;
	static String query = null;

	public Connection connect() {

		// variables
		final String dbUrl = "jdbc:mysql://localhost:3306/inloggning";
		final String dbUsername = "root";
		final String dbPassword = "";

		try {

			// driver path
			Class.forName("com.mysql.cj.jdbc.Driver");

			con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

//			// check connection
//			if (con == null) {
//				System.out.println("JDBC connection is not established");
//
//			} else {
//				System.out.println("Connection Successfully");
//			}

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		} catch (Exception ex) {
			// handle the error of the dirver
			System.out.println("Exception Driver: " + ex);

		}

		return con;

	}

	public LoginBean checkLogin(String email, String password) {

		LoginBean loginbean = null;

		try {

			query = "SELECT * FROM users WHERE email = ? AND password = ?";

			con = connect();

			stmt = con.prepareStatement(query);

			stmt.setString(1, email);
			stmt.setString(2, password);

			rs = stmt.executeQuery();

			while (rs.next()) {
				loginbean = new LoginBean();
				loginbean.setUsers_id(rs.getInt("users_id"));
				loginbean.setEmail(email);
				loginbean.setPassword(password);
				loginbean.setFirstName(rs.getString("FirstName"));
				loginbean.setLastName(rs.getString("lastName"));
				loginbean.setUserType(rs.getString("userType"));

			}

			con.close();

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		} catch (Exception ex) {
			// handle the error
			System.out.println("Exception Driver: " + ex);

		}

		return loginbean;

	}

	public LoginBean getPersonId(int id) {

		LoginBean user = null;

		try {
			query = "SELECT users_id FROM users WHERE users_id = ?";
			con = connect();

			stmt = con.prepareStatement(query);
			
			stmt.setInt(1, id);

			rs = stmt.executeQuery();

			while (rs.next()) {
				user = new LoginBean();
				
				user.setUsers_id(id);
				
			}

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		return user;
	}
}