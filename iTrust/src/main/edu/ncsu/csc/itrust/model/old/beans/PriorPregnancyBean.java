package edu.ncsu.csc.itrust.model.old.beans;

import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;

/**
 * A bean for storing data about the prior pregnancy record of a patient.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class PriorPregnancyBean implements Comparable<PriorPregnancyBean>
{
	private long priorPregnancyID;
	private long obstetricRecordID;
	private int yearOfConception;
	private int daysPregnant;
	private float hoursInLabor;
	private float weightGain;
	private DeliveryType deliveryType;
	private int multiplicity;

	public PriorPregnancyBean() {
		priorPregnancyID = 0;
		obstetricRecordID = 0;
		yearOfConception = 0;
		daysPregnant = 0;
		hoursInLabor = 0f;
		weightGain = 0f;
		deliveryType = DeliveryType.NotSpecified;
		multiplicity = 0;
	}

	public long getObstetricRecordID() {
		return obstetricRecordID;
	}

	public void setObstetricRecordID(long obstetricRecordID) {
		this.obstetricRecordID = obstetricRecordID;
	}

	public long getPriorPregnancyID()
	{
		return priorPregnancyID;
	}

	public void setPriorPregnancyID(long priorPregnancyID)
	{
		this.priorPregnancyID = priorPregnancyID;
	}

	public int getYearOfConception()
	{
		return yearOfConception;
	}
	
	public void setYearOfConception(int yearOfConception)
	{
		this.yearOfConception = yearOfConception;
	}
	
	public int getDaysPregnant()
	{
		return daysPregnant;
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
	
	public void setDaysPregnant(int daysPregnant)
	{
		this.daysPregnant = daysPregnant;
	}
	
	public void setWeeksDaysPregnant(String weeksDaysPregnant)
	{
		String[] weeksDaysPregnantSplit = weeksDaysPregnant.split("-");
		String weeksPregnant = weeksDaysPregnantSplit[0];
		String daysPregnant = weeksDaysPregnantSplit[1];

		this.daysPregnant = Integer.parseInt(weeksPregnant) * 7 + Integer.parseInt(daysPregnant);
	}
	
	public float getHoursInLabor()
	{
		return hoursInLabor;
	}
	
	public void setHoursInLabor(float hoursInLabor)
	{
		this.hoursInLabor = hoursInLabor;
	}
	
	public float getWeightGain()
	{
		return weightGain;
	}
	
	public void setWeightGain(float weightGain)
	{
		this.weightGain = weightGain;
	}
	
	public DeliveryType getDeliveryType()
	{
		return deliveryType;
	}

	public void setDeliveryTypeStr(String deliveryType)
	{
		this.deliveryType = DeliveryType.parse(deliveryType);
	}

	public void setDeliveryType(DeliveryType deliveryType)
	{
		this.deliveryType = deliveryType;
	}
	
	public int getMultiplicity()
	{
		return multiplicity;
	}
	
	public void setMultiplicity(int multiplicity)
	{
		this.multiplicity = multiplicity;
	}

	@Override
	public int compareTo(PriorPregnancyBean o)
	{
		return (int)(o.priorPregnancyID - this.priorPregnancyID);
	}

	@Override
	public String toString() {
		return String.format("{\"yearOfConception\":\"%d\", \"daysPregnant\":\"%d\", \"hoursInLabor\":\"%.2f\", \"weightGain\": \"%.2f\", \"deliveryType\":\"%s\", \"multiplicity\": \"%d\", \"recordID\": \"%d\"}",
				this.getYearOfConception(),
				this.getDaysPregnant(),
				this.getHoursInLabor(),
				this.getWeightGain(),
				this.getDeliveryType().toString(),
				this.getMultiplicity(),
				this.getPriorPregnancyID());
	}
}
