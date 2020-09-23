package edu.ncsu.csc.itrust.model.old.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * A bean for storing data about the obstetrics initialization record of a patient.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class ObstetricsInitBean implements Comparable<ObstetricsInitBean>
{
	private long recordID ;
	private long patientMID;
	private Date initDate;
	private Date lastMenstrualPeriod;
	/**
	 * Used to display as output to the user.
	 */
	private final SimpleDateFormat MMDDYYYY_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	/**
	 * Used to set date picker value in HTML since they only accept this pattern.
	 */
	private final SimpleDateFormat YYYYMMDD_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public ObstetricsInitBean() {
		this.recordID = 0;
		this.patientMID = 0;
		this.initDate = null;
		this.lastMenstrualPeriod = null;
	}

	public long getRecordID()
	{
		return recordID;
	}

	public void setRecordID(long recordID)
	{
		this.recordID = recordID;
	}
	
	public long getPatientMID()
	{
		return patientMID;
	}

	public void setPatientMID(long patientMID)
	{
		this.patientMID = patientMID;
	}
	
	public Date getInitDate() {
		return initDate;
	}
	
	public String getInitDateMMDDYYYY() {
		if(this.initDate == null) {
			return  null;
		}
		return MMDDYYYY_FORMAT.format(this.initDate);
	}

	public String getInitDateYYYYMMDD() {
		if(this.initDate == null) {
			return  null;
		}
		return YYYYMMDD_FORMAT.format(this.initDate);
	}
	
	public void setInitDate(String date, String pattern)
	{
		try
		{
			this.initDate = new SimpleDateFormat(pattern).parse(date);
		}
		catch (ParseException e)
		{
			this.initDate = null;
		}
	}

	public void setInitDate(Date date) {
		this.initDate = date;
	}
	
	public String getLastMenstrualPeriodMMDDYYYY() {
		if (this.lastMenstrualPeriod == null) {
			return null;
		}
		return MMDDYYYY_FORMAT.format(this.lastMenstrualPeriod);
	}

	public String getLastMenstrualPeriodYYYYMMDD() {
		if (this.lastMenstrualPeriod == null) {
			return null;
		}
		return YYYYMMDD_FORMAT.format(this.lastMenstrualPeriod);
	}

	public void setLastMenstrualPeriod(String date, String pattern) {
		try
		{
			this.lastMenstrualPeriod = new SimpleDateFormat(pattern).parse(date);
		}
		catch (ParseException e)
		{
			this.lastMenstrualPeriod = null;
		}
	}

	public void setLastMenstrualPeriod(Date date) {
		this.lastMenstrualPeriod = date;
	}

	public Date getLastMenstrualPeriod() {
		return this.lastMenstrualPeriod;
	}
	
	public Date getEstimatedDueDate() {
		Date lastMenstrualPeriodDate = getLastMenstrualPeriod();
		if(lastMenstrualPeriodDate == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lastMenstrualPeriodDate);
		calendar.add(Calendar.DATE, 280);
		
		return calendar.getTime();
	}
	
	public String getEstimatedDueDateMMDDYYYY()
	{
		Date eddDate = getEstimatedDueDate();
		if(eddDate == null) {
			return null;
		}

		return MMDDYYYY_FORMAT.format(eddDate);
	}
	
	public long getDaysPregnant()
	{
		Date lastMenstrualPeriodDate = getLastMenstrualPeriod();
		if(lastMenstrualPeriodDate == null) {
			return 0;
		}
		long diff = initDate.getTime() - lastMenstrualPeriodDate.getTime();

		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	/**
	 * This will output weeks and days pregnant as a string based on how many total days pregnant.
	 * ex.  1 week, 0 days
	 * 		2 weeks, 1 day
	 * 		0 weeks, 3 days
	 * @return formatted string output for weeks and days pregnant
	 */
	public String getWeeksDaysPregnant(){
		long totalDays = this.getDaysPregnant();
		if(totalDays == 0) {
			return "0 days";
		}
		long numWeeks = totalDays / 7;
		long numDays = totalDays % 7;

		StringBuilder builder = new StringBuilder();

		builder.append(numWeeks);
		if (numWeeks == 1) {
			builder.append(" week, ");
		} else {
			builder.append(" weeks, ");
		}

		builder.append(numDays);
		if (numDays == 1) {
			builder.append(" day");
		} else {
			builder.append(" days");
		}

		return builder.toString();
	}

	@Override
	public int compareTo(ObstetricsInitBean o)
	{
		return (int)(o.recordID - this.recordID);
	}

	@Override
	public String toString() {
		return
			String.format("{\"recordID\":\"%d\", \"initDate\":\"%s\", \"lmpDate\":\"%s\", \"edDate\": \"%s\", \"weeksDaysPregnant\":\"%s\", \"initDateInput\": \"%s\", \"lmpDateInput\":\"%s\"}",
					this.getRecordID(),
                    this.getInitDateMMDDYYYY(),
					this.getLastMenstrualPeriodMMDDYYYY(),
					this.getEstimatedDueDateMMDDYYYY(),
					this.getWeeksDaysPregnant(),
					this.getInitDateYYYYMMDD(),
					this.getLastMenstrualPeriodYYYYMMDD());
	}
}
