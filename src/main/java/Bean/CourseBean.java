package Bean;

import java.util.Date;

public class CourseBean {
	
	private int kursId;
	private String kursNamn;
	private Date startDatum;
	private Date slutDatum;
	private int utbId;
	
	public int getKursId() {
		return kursId;
	}
	
	public void setKursId(int kursId) {
		this.kursId = kursId;
	}
	
	public String getKursNamn() {
		return kursNamn;
	}
	
	public void setKursNamn(String kursNamn) {
		this.kursNamn = kursNamn;
	}
	
	public Date getStartDatum() {
		return startDatum;
	}
	
	public void setStartDatum(Date startDatum) {
		this.startDatum = startDatum;
	}
	
	public Date getSlutDatum() {
		return slutDatum;
	}
	
	public void setSlutDatum(Date slutDatum) {
		this.slutDatum = slutDatum;
	}
	
	public int getUtbId() {
		return utbId;
	}
	
	public void setUtbId(int utbId) {
		this.utbId = utbId;
	}
}