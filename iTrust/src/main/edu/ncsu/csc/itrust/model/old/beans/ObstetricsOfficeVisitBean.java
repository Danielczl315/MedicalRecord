package edu.ncsu.csc.itrust.model.old.beans;

import edu.ncsu.csc.itrust.model.old.enums.BooleanType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * A bean for storing data about an obstetrics office visit.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */

public class ObstetricsOfficeVisitBean implements Comparable<ObstetricsOfficeVisitBean> {

	private long visitID = 0;
	private long obstetricsInitRecordID = 0;
	private long locationID = 0;
	private long patientMID = 0;
	private long hcpMID = 0;
	private long apptID = 0;
	private String visitDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
	
	private final SimpleDateFormat visitDate_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	private float weight = 0.0f;
	private int bloodPressure = 0;
	private int fetalHeartRate = 0;
	private BooleanType lowLyingPlacentaObserved = BooleanType.NotSpecified;
	private int numberOfBabies = 0;

	//Used in the response to calculate weeks pregnant
	private Date lastMenstrualPeriodDate = null;


	public void setLastMenstrualPeriodDate(Date lastMenstrualPeriodDate) {
		this.lastMenstrualPeriodDate = lastMenstrualPeriodDate;
	}

	/**
	 * @return the visitID
	 */
	public long getVisitID() {
		return visitID;
	}

	/**
	 * @param visitID the visitID to set
	 */
	public void setVisitID(long visitID) {
		this.visitID = visitID;
	}

	/**
	 * @return the obstetricsInitRecordID
	 */
	public long getObstetricsInitRecordID() {
		return obstetricsInitRecordID;
	}

	/**
	 * @param obstetricsInitRecordID the obstetricsInitRecordID to set
	 */
	public void setObstetricsInitRecordID(long obstetricsInitRecordID) {
		this.obstetricsInitRecordID = obstetricsInitRecordID;
	}

	/**
	 * @return the locationID
	 */
	public long getLocationID() {
		return locationID;
	}

	/**
	 * @param locationID the locationID to set
	 */
	public void setLocationID(long locationID) {
		this.locationID = locationID;
	}

	/**
	 * @return the patientMID
	 */
	public long getPatientMID() {
		return patientMID;
	}

	/**
	 * @param patientMID the patientMID to set
	 */
	public void setPatientMID(long patientMID) {
		this.patientMID = patientMID;
	}

	/**
	 * @return the hcpMID
	 */
	public long getHcpMID() {
		return hcpMID;
	}

	/**
	 * @param hcpMID the hcpMID to set
	 */
	public void setHcpMID(long hcpMID) {
		this.hcpMID = hcpMID;
	}

	/**
	 * @return the apptID
	 */
	public long getApptID() {
		return apptID;
	}

	/**
	 * @param apptID the apptID to set
	 */
	public void setApptID(long apptID) {
		this.apptID = apptID;
	}

	/**
	 * @return the visitDate as a string
	 */
	public String getVisitDateStr() {
		return visitDate;
	}

	public String getVisitDateStr(String pattern) {
		Date date = getVisitDate();
		return new SimpleDateFormat(pattern).format(date);
	}
	/**
	 * @return the visitDate as a java.util.Date object
	 */
	public Date getVisitDate() {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(visitDate);
		}
		catch (ParseException e) {
			return null;
		}
	}
	
	public int getVisitDate_yyyyMMdd() {
		if (getVisitDate() == null) {
			return 0;
		}
		else {
			Date temp_date = getVisitDate();
			return Integer.parseInt(visitDate_yyyyMMdd.format(temp_date));
		}
		
	}

	/**
	 * @param visitDateStr string representing the visitDate to set
	 */
	public void setVisitDateStr(String visitDateStr) {
		this.visitDate = visitDateStr;
	}
	/**
	 * @param visitDateStr string representing the visitDate to set
	 */
	public void setVisitDateStr(String visitDateStr, String pattern) {
		try {
			Date date = new SimpleDateFormat(pattern).parse(visitDateStr);
			this.visitDate = new SimpleDateFormat("MM/dd/yyyy").format(date);
		}
		catch (ParseException e) {
			return;
		}
	}
	/**
	 * @return weight
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * @return the bloodPressure
	 */
	public int getBloodPressure() {
		return bloodPressure;
	}


	/**
	 * @param bloodPressure the bloodPressure to set
	 */
	public void setBloodPressure(int bloodPressure) {
		this.bloodPressure = bloodPressure;
	}


	/**
	 * @return the fetalHeartRate
	 */
	public int getFetalHeartRate() {
		return fetalHeartRate;
	}


	/**
	 * @param fetalHeartRate the fetalHeartRate to set
	 */
	public void setFetalHeartRate(int fetalHeartRate) {
		this.fetalHeartRate = fetalHeartRate;
	}
	
	/**
	 * @return the lowLyingPlacentaObserved
	 */
	public BooleanType getLowLyingPlacentaObserved() {
		return lowLyingPlacentaObserved;
	}

	/**
	 * @param lowLyingPlacentaObserved the lowLyingPlacentaObserved to set
	 */
	public void setLowLyingPlacentaObserved(BooleanType lowLyingPlacentaObserved) {
		this.lowLyingPlacentaObserved = lowLyingPlacentaObserved;
	}
	
	/**
	 * @param lowLyingPlacentaObservedStr string that parses into the lowLyingPlacentaObserved to set
	 */
	public void setLowLyingPlacentaObservedStr(String lowLyingPlacentaObservedStr) {
		this.lowLyingPlacentaObserved = BooleanType.parse(lowLyingPlacentaObservedStr);
	}

	/**
	 * @return the numberOfBabies
	 */
	public int getNumberOfBabies() {
		return numberOfBabies;
	}

	/**
	 * @param numberOfBabies the numberOfBabies to set
	 */
	public void setNumberOfBabies(int numberOfBabies) {
		this.numberOfBabies = numberOfBabies;
	}

	/**
	 * This will output weeks and days pregnant as a string based on how many total days pregnant.
	 * ex.  1 week, 0 days
	 * 		2 weeks, 1 day
	 * 		0 weeks, 3 days
	 * @return formatted string output for weeks and days pregnant
	 */
	public String getWeeksDaysPregnant(){
		if(lastMenstrualPeriodDate == null) {
			return "0";
		}
		long diff = getVisitDate().getTime() - lastMenstrualPeriodDate.getTime();

		long totalDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

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
	public int compareTo(ObstetricsOfficeVisitBean oov)
	{
		return (int)(oov.getVisitDate().compareTo(this.getVisitDate()));
	}
	
	public int equals(ObstetricsOfficeVisitBean oov)
	{
		return (int)(oov.getVisitDate().compareTo(this.getVisitDate()));
	}

    @Override
    public String toString() {
        return
                String.format(" {\"visitID\":\"%d\", \"officeVisitDate\":\"%s\", \"officeVisitDateInput\":\"%s\", " +
                                "\"weight\": \"%.2f\", \"weeksDaysPregnant\":\"%s\", \"bloodPressure\": \"%d\", " +
                                "\"fetalHeartRate\":\"%d\", " +
                                "\"numBabies\":\"%d\", " +
                                "\"lowLyingPlacenta\":\"%s\"} " ,
                        this.getVisitID(),
                        this.getVisitDateStr("MM/dd/yyyy"),
                        this.getVisitDateStr("yyyy-MM-dd"),
                        this.getWeight(),
                        this.getWeeksDaysPregnant(),
                        this.getBloodPressure(),
                        this.getFetalHeartRate(),
                        this.getNumberOfBabies(),
                        this.getLowLyingPlacentaObserved().getName());
    }
}
