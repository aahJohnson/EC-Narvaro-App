package Bean;

import java.util.Date;

public class LessonBean {
	
	private int lessonId;
	private Date date;
	private int courseId;
	private int minutes;
	

	public int getMinuter() {
		return minutes;
	}

	public void setMinuter(int minuter) {
		this.minutes = minuter;
	}

	public int getLekId() {
		return lessonId;
	}
	
	public void setLekId(int lekId) {
		this.lessonId = lekId;
	}
	
	public Date getDatum() {
		return date;
	}
	
	public void setDatum(Date datum) {
		this.date = datum;
	}
	
	public int getKursId() {
		return courseId;
	}
	
	public void setKursId(int kursId) {
		this.courseId = kursId;
	}

	@Override
	public String toString() {
		return "LektionBean [lekId=" + lessonId + ", datum=" + date + ", kursId=" + courseId + ", minuter=" + minutes + "]";
	}
}