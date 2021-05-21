package conDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Bean.LessonBean;
import Bean.AttendanceBean;
import Bean.PersonBean;
import Bean.CourseBean;

public class AttendanceDAO {

	// Declaring the variables
	static Connection con = null;
	static PreparedStatement stmt = null;
	static ResultSet rs = null;
	static String query = null;

	// Connecting to database
	public Connection connect() {

		// variables
		final String dbUrl = "jdbc:mysql://localhost:3306/narvaro";
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
			// handle the error of the driver
			System.out.println("Exception Driver: " + ex);

		}

		return con;

	}

	public ArrayList<AttendanceBean> personInformation() {

		PersonBean personBeanInstance = null;
		LessonBean lessonBeanInstance = null;
		CourseBean courseBeanInstance = null;

		ArrayList<AttendanceBean> list = new ArrayList<AttendanceBean>();

		try {
			query = "SELECT p.PersonId PersonId, l.lekId LekId, l.datum Datum, l.Minuter Minuter, k.kursId KursId, n.Andel Andel FROM person p INNER JOIN narvaro n ON p.PersonId = n.PersonId "
					+ "INNER JOIN lektion l ON l.lekId = n.lekId INNER JOIN kurs k ON k.KursId = l.KursId";

			con = connect();

			stmt = con.prepareStatement(query);

			rs = stmt.executeQuery();

			while (rs.next()) {
				AttendanceBean attendanceBeanInstance = new AttendanceBean();
				personBeanInstance = new PersonBean();
				lessonBeanInstance = new LessonBean();
				courseBeanInstance = new CourseBean();
//				perBean.setFirstName(rs.getString("firstName"));
//				perBean.setLastName(rs.getString("lastName"));
				personBeanInstance.setPersonId(rs.getInt("PersonId"));
				lessonBeanInstance.setLekId(rs.getInt("LekId"));
				lessonBeanInstance.setMinuter(rs.getInt("Minuter"));
				lessonBeanInstance.setDatum(rs.getDate("Datum"));
				lessonBeanInstance.setKursId(rs.getInt("KursId"));
				attendanceBeanInstance.setAndel(rs.getInt("Andel"));
				
				attendanceBeanInstance.setPerson(personBeanInstance);
				attendanceBeanInstance.setLektion(lessonBeanInstance);
				list.add(attendanceBeanInstance);

			}
			
			rs.close();
			con.close();

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		return list;

	}

	public ArrayList<CourseBean> courseInformation() {

		ArrayList<CourseBean> courses = new ArrayList<CourseBean>();

		CourseBean courseBeanInstance = null;

		try {

			query = "SELECT * FROM kurs";
			con = connect();
			stmt = con.prepareStatement(query);

			rs = stmt.executeQuery();

			while (rs.next()) {
				courseBeanInstance = new CourseBean();
				courseBeanInstance.setKursId(rs.getInt("KursId"));
				courseBeanInstance.setKursNamn(rs.getString("KursNamn"));
				courseBeanInstance.setStartDatum(rs.getDate("startDatum"));
				courseBeanInstance.setSlutDatum(rs.getDate("slutDatum"));
				courses.add(courseBeanInstance);

			}
			
			rs.close();
			con.close();

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
		return courses;

	}

	public ArrayList<LessonBean> lessonInformation() {

		ArrayList<LessonBean> lessons = new ArrayList<LessonBean>();

		LessonBean lessonBeanInstance = null;

		try {

			query = "SELECT * FROM lektion";

			con = connect();

			stmt = con.prepareStatement(query);

			rs = stmt.executeQuery();

			while (rs.next()) {
				lessonBeanInstance = new LessonBean();
				lessonBeanInstance.setDatum(rs.getDate("datum"));
				lessonBeanInstance.setKursId(rs.getInt("KursId"));
				lessonBeanInstance.setLekId(rs.getInt("lekId"));
				lessonBeanInstance.setMinuter(rs.getInt("minuter"));
				lessons.add(lessonBeanInstance);

			}
			
			rs.close();
			con.close();

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}

		return lessons;

	}
	
	public LessonBean lessonId(int id) {
		
		LessonBean lessonBeanInstance = null;
		
		try {
			
			query = "SELECT * FROM lektion WHERE lekId = ?";
			con = connect();
			stmt = con.prepareStatement(query);
			
			stmt.setInt(1, id);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				lessonBeanInstance = new LessonBean();
				
				lessonBeanInstance.setLekId(rs.getInt("lekId"));
				lessonBeanInstance.setMinuter(rs.getInt("Minuter"));
			}
			
			rs.close();
			con.close();
			
		}
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
		
		return lessonBeanInstance;
	}
	
	public boolean updateAttendance(int personId, int lessonId, int attendance) {
		
		try {
			
			query = "UPDATE narvaro SET Andel = ? WHERE PersonId = ? AND lekId = ?";
			con = connect();
			stmt = con.prepareStatement(query);
			
			stmt.setInt(1, attendance);
			stmt.setInt(2, personId);
			stmt.setInt(3, lessonId);
			
			stmt.executeUpdate();
			
			con.close();
			
			return true;
			
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
		
		return false;
	}
}