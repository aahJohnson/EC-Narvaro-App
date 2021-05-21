package Bean;

import java.util.Date;

public class CourseBean {
	
	private int courseId;
	private String courseName;
	private Date startDate;
	private Date endDate;
	private int educationId;
	
	public int getKursId() {
		return courseId;
	}
	
	public void setKursId(int kursId) {
		this.courseId = kursId;
	}
	
	public String getKursNamn() {
		return courseName;
	}
	
	public void setKursNamn(String kursNamn) {
		this.courseName = kursNamn;
	}
	
	public Date getStartDatum() {
		return startDate;
	}
	
	public void setStartDatum(Date startDatum) {
		this.startDate = startDatum;
	}
	
	public Date getSlutDatum() {
		return endDate;
	}
	
	public void setSlutDatum(Date slutDatum) {
		this.endDate = slutDatum;
	}
	
	public int getUtbId() {
		return educationId;
	}
	
	public void setUtbId(int utbId) {
		this.educationId = utbId;
	}
}