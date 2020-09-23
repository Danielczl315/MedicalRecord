package edu.ncsu.csc.itrust.model.old.beans;

import edu.ncsu.csc.itrust.model.old.enums.BooleanType;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.Gender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A bean for storing data about the baby delivery info record of a patient.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class BabyDeliveryInfoBean implements Comparable<BabyDeliveryInfoBean>
{
	private long recordID = 0;
	private long childBirthVisitID = 0;
	private long patientMID = 0;
	private Gender gender = Gender.NotSpecified;
	private Date birthDate = new Date();
	private String dateOfBirthStr = new SimpleDateFormat("MM/dd/yyyy").format(birthDate);
	private DeliveryType deliveryMethod = DeliveryType.NotSpecified;
	private BooleanType isEstimated = BooleanType.NotSpecified;


	public long getPatientMID()
	{
		return patientMID;
	}

	public void setPatientMID(long patientMID)
	{
		this.patientMID = patientMID;
	}

	public long getRecordID()
	{
		return recordID;
	}

	public void setRecordID(long recordID)
	{
		this.recordID = recordID;
	}
	
	public long getChildBirthVisitID()
	{
		return childBirthVisitID;
	}

	public void setChildBirthVisitID(long childBirthVisitID)
	{
		this.childBirthVisitID = childBirthVisitID;
	}

	public Gender getGender()
	{
		return gender;
	}

	public void setGenderStr(String gender)
	{
		this.gender = Gender.parse(gender);
	}

	public void setGender(Gender gender)
	{
		this.gender = gender;
	}
	
	public String getDateOfBirthStr()
	{
		return dateOfBirthStr;
	}

	public Date getDateOfBirth()
	{
		return birthDate;
	}

	public void setDateOfBirthStr(String dateOfBirthStr)
	{

		try
		{
			this.birthDate = new SimpleDateFormat("MM/dd/yyyy").parse(dateOfBirthStr);
			this.dateOfBirthStr = dateOfBirthStr;
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			this.birthDate = new Date();
			this.dateOfBirthStr = new SimpleDateFormat("MM/dd/yyyy").format(birthDate);
		}
	}

	public void setDateOfBirthStr(String dateOfBirthStr, String pattern)
	{

		try
		{
			this.birthDate = new SimpleDateFormat(pattern).parse(dateOfBirthStr);
			this.dateOfBirthStr = new SimpleDateFormat("MM/dd/yyyy").format(birthDate);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			this.birthDate = new Date();
			this.dateOfBirthStr = new SimpleDateFormat("MM/dd/yyyy").format(birthDate);
		}
	}

	public String getBirthDatePattern(String pattern) {
		return new SimpleDateFormat(pattern).format(birthDate);
	}

	public DeliveryType getDeliveryMethod()
	{
		return deliveryMethod;
	}

	public void setDeliveryMethodStr(String deliveryMethod)
	{
		this.deliveryMethod = DeliveryType.parse(deliveryMethod);
	}

	public void setDeliveryMethod(DeliveryType deliveryMethod)
	{
		this.deliveryMethod = deliveryMethod;
	}
	
	public BooleanType getIsEstimated()
	{
		return isEstimated;
	}

	public void setIsEstimatedStr(String isEstimated)
	{
		this.isEstimated = BooleanType.parse(isEstimated);
	}

	public void setIsEstimated(BooleanType isEstimated)
	{
		this.isEstimated = isEstimated;
	}
	
	@Override
	public int compareTo(BabyDeliveryInfoBean o)
	{
		return (int)(o.recordID - this.recordID);
	}
	
	public int equals(BabyDeliveryInfoBean o)
	{
		return (int)(o.recordID - this.recordID);
	}
	
	@Override
	public int hashCode()
	{
		assert false : "hashCode not designed";
		return 42;
	}

	@Override
	public String toString() {
		String birthDateInput = this.getBirthDatePattern("yyyy-MM-dd") + "T" + this.getBirthDatePattern("HH:mm");
		return String.format("{\"recordID\":\"%d\", \"birthDate\":\"%s\", \"deliveryType\":\"%s\", " +
						"\"sexType\": \"%s\", \"birthDateInput\": \"%s\"}",
				this.getRecordID(),
				this.getBirthDatePattern("MM/dd/yyyy HH:mm aa"),
				this.getDeliveryMethod().getName(),
				this.getGender().getName(),
				birthDateInput);
	}
}