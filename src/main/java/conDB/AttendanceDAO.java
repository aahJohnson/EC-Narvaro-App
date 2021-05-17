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

	// ADAM
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

	// HOI
	public ArrayList<AttendanceBean> narvaroPerson() {

		PersonBean perBean = null;
		LessonBean lekBean = null;
		CourseBean courseBean = null;

		ArrayList<AttendanceBean> list = new ArrayList<AttendanceBean>();

		try {
			query = "SELECT p.PersonId PersonId, l.lekId LekId, l.datum Datum, l.Minuter Minuter, k.kursId KursId, n.Andel Andel FROM person p INNER JOIN narvaro n ON p.PersonId = n.PersonId "
					+ "INNER JOIN lektion l ON l.lekId = n.lekId INNER JOIN kurs k ON k.KursId = l.KursId";

			con = connect();

			stmt = con.prepareStatement(query);

			rs = stmt.executeQuery();

			while (rs.next()) {
				AttendanceBean narvaro = new AttendanceBean();
				perBean = new PersonBean();
				lekBean = new LessonBean();
				courseBean = new CourseBean();
//				perBean.setFirstName(rs.getString("firstName"));
//				perBean.setLastName(rs.getString("lastName"));
				perBean.setPersonId(rs.getInt("PersonId"));
				lekBean.setLekId(rs.getInt("LekId"));
				lekBean.setMinuter(rs.getInt("Minuter"));
				lekBean.setDatum(rs.getDate("Datum"));
				lekBean.setKursId(rs.getInt("KursId"));
				narvaro.setAndel(rs.getInt("Andel"));
				
				narvaro.setPerson(perBean);
				narvaro.setLektion(lekBean);
				list.add(narvaro);

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

	public ArrayList<CourseBean> kurs() {

		ArrayList<CourseBean> listKurs = new ArrayList<CourseBean>();

		CourseBean kurs = null;

		try {

			query = "SELECT * FROM kurs";
			con = connect();
			stmt = con.prepareStatement(query);

			rs = stmt.executeQuery();

			while (rs.next()) {
				kurs = new CourseBean();
				kurs.setKursId(rs.getInt("KursId"));
				kurs.setKursNamn(rs.getString("KursNamn"));
				kurs.setStartDatum(rs.getDate("startDatum"));
				kurs.setSlutDatum(rs.getDate("slutDatum"));
				listKurs.add(kurs);

			}
			
			rs.close();
			con.close();

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
		return listKurs;

	}

	public ArrayList<LessonBean> lektionBeans() {

		ArrayList<LessonBean> lessons = new ArrayList<LessonBean>();

		LessonBean lekBean = null;

		try {

			query = "SELECT * FROM lektion";

			con = connect();

			stmt = con.prepareStatement(query);

			rs = stmt.executeQuery();

			while (rs.next()) {
				lekBean = new LessonBean();
				lekBean.setDatum(rs.getDate("datum"));
				lekBean.setKursId(rs.getInt("KursId"));
				lekBean.setLekId(rs.getInt("lekId"));
				lekBean.setMinuter(rs.getInt("minuter"));
				lessons.add(lekBean);

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
		
		LessonBean lesson = null;
		
		
		try {
			
			query = "SELECT * FROM lektion WHERE lekId = ?";
			con = connect();
			stmt = con.prepareStatement(query);
			
			stmt.setInt(1, id);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				 lesson = new LessonBean();
				
				lesson.setLekId(rs.getInt("lekId"));
				lesson.setMinuter(rs.getInt("Mintuer"));
			}
			
			rs.close();
			con.close();
			
		}catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
		return lesson;
		
		
		
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