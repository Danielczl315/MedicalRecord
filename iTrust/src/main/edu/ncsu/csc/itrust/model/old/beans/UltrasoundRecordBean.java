package edu.ncsu.csc.itrust.model.old.beans;

/**
 * A bean for storing data about the ultrasound record of a patient.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class UltrasoundRecordBean implements Comparable<UltrasoundRecordBean>
{
	private long recordID = 0;
	private long obstetricsOfficeVisitID = 0;
	private float crownRumpLength = 0.0f;
	private float biparietalDiameter = 0.0f;
	private float headCircumference = 0.0f;
	private float femurLength = 0.0f;
	private float occipitofrontalDiameter = 0.0f;
	private float abdominalCircumference = 0.0f;
	private float humerusLength = 0.0f;
	private float estimatedFetalWeight = 0.0f;
	private String fileURL = "";
	
	public long getRecordID()
	{
		return recordID;
	}

	public void setRecordID(long recordID)
	{
		this.recordID = recordID;
	}
	
	public long getObstetricsOfficeVisitID()
	{
		return obstetricsOfficeVisitID;
	}

	public void setObstetricsOfficeVisitID(long obstetricsOfficeVisitID)
	{
		this.obstetricsOfficeVisitID = obstetricsOfficeVisitID;
	}
	
	public float getCrownRumpLength()
	{
		return crownRumpLength;
	}
	
	public void setCrownRumpLength(float crownRumpLength)
	{
		this.crownRumpLength = crownRumpLength;
	}
	
	public float getBiparietalDiameter()
	{
		return biparietalDiameter;
	}
	
	public void setBiparietalDiameter(float biparietalDiameter)
	{
		this.biparietalDiameter = biparietalDiameter;
	}
	
	public float getHeadCircumference()
	{
		return headCircumference;
	}
	
	public void setHeadCircumference(float headCircumference)
	{
		this.headCircumference = headCircumference;
	}
	
	public float getFemurLength()
	{
		return femurLength;
	}
	
	public void setFemurLength(float femurLength)
	{
		this.femurLength = femurLength;
	}
	
	public float getOccipitofrontalDiameter()
	{
		return occipitofrontalDiameter;
	}
	
	public void setOccipitofrontalDiameter(float occipitofrontalDiameter)
	{
		this.occipitofrontalDiameter = occipitofrontalDiameter;
	}
	
	public float getAbdominalCircumference()
	{
		return abdominalCircumference;
	}
	
	public void setAbdominalCircumference(float abdominalCircumference)
	{
		this.abdominalCircumference = abdominalCircumference;
	}

	public float getHumerusLength()
	{
		return humerusLength;
	}
	
	public void setHumerusLength(float humerusLength)
	{
		this.humerusLength = humerusLength;
	}
	
	public float getEstimatedFetalWeight()
	{
		return estimatedFetalWeight;
	}
	
	public void setEstimatedFetalWeight(float estimatedFetalWeight)
	{
		this.estimatedFetalWeight = estimatedFetalWeight;
	}
	
	public String getFileURL()
	{
		return fileURL;
	}
	
	public void setFileURL(String fileURL)
	{
		this.fileURL = fileURL;
	}

	@Override
	public int compareTo(UltrasoundRecordBean o)
	{
		return (int)(o.recordID - this.recordID);
	}
	
	public int equals(UltrasoundRecordBean o)
	{
		return (int)(o.recordID - this.recordID);
	}

	@Override
	public String toString() {
		return String.format(" {\"recordID\":\"%d\", \"crownRumpLength\":\"%.2f\", \"bpDiameter\":\"%.2f\", " +
								"\"headCircumference\": \"%.2f\", \"femurLength\":\"%.2f\", \"ofDiameter\": \"%.2f\", " +
								"\"abCircumference\":\"%.2f\", " +
								"\"humerusLength\":\"%.2f\", " +
								"\"estFetalWeight\":\"%.2f\", \"fileURL\":\"%s\"} " ,
						this.getRecordID(),
						this.getCrownRumpLength(),
						this.getBiparietalDiameter(),
						this.getHeadCircumference(),
						this.getFemurLength(),
						this.getOccipitofrontalDiameter(),
						this.getAbdominalCircumference(),
						this.getHumerusLength(),
						this.getEstimatedFetalWeight(),
						this.getFileURL());
	}
}
