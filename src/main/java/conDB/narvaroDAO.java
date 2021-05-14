package conDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Bean.LessonBean;
import Bean.NarvaroBean;
import Bean.PersonBean;
import Bean.CourseBean;

public class narvaroDAO {

	// Declaring the variables
	static Connection con = null;
	static PreparedStatement stmt = null;
	static ResultSet rs = null;
	static String query = null;

	public Connection connect() {

		// variables
		final String dbUrl = "jdbc:mysql://localhost:3306/narvaro";
		final String dbUsername = "root";
		final String dbPassword = "";

		try {

			// driver path
			Class.forName("com.mysql.cj.jdbc.Driver");

			con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

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

	public ArrayList<NarvaroBean> narvaroPerson() {

		PersonBean perBean = null;
		LessonBean lekBean = null;
		CourseBean courseBean = null;

		ArrayList<NarvaroBean> list = new ArrayList<NarvaroBean>();

		try {
			query = "SELECT p.PersonId PersonId, l.lekId LekId, l.datum Datum, l.Minuter Minuter, k.kursId KursId, n.Andel Andel FROM person p INNER JOIN narvaro n ON p.PersonId = n.PersonId "
					+ "INNER JOIN lektion l ON l.lekId = n.lekId INNER JOIN kurs k ON k.KursId = l.KursId";

			con = connect();

			stmt = con.prepareStatement(query);

			rs = stmt.executeQuery();

			while (rs.next()) {
				NarvaroBean narvaro = new NarvaroBean();
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

	public ArrayList<CourseBean> courses() {

		ArrayList<CourseBean> courseList = new ArrayList<CourseBean>();

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
				courseList.add(courseBeanInstance);

			}
			
			rs.close();
			con.close();

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
		return courseList;

	}

	public ArrayList<LessonBean> lessions() {

		ArrayList<LessonBean> lessons = new ArrayList<LessonBean>();

		LessonBean lessionBeanInstance = null;

		try {

			query = "SELECT * FROM lektion";

			con = connect();

			stmt = con.prepareStatement(query);

			rs = stmt.executeQuery();

			while (rs.next()) {
				lessionBeanInstance = new LessonBean();
				lessionBeanInstance.setDatum(rs.getDate("datum"));
				lessionBeanInstance.setKursId(rs.getInt("kursId"));
				lessionBeanInstance.setLekId(rs.getInt("lekId"));
				lessionBeanInstance.setMinuter(rs.getInt("minuter"));
				lessons.add(lessionBeanInstance);

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