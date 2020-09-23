package edu.ncsu.csc.itrust.model.old.beans;

import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.BooleanType;

/**
 * A bean for storing data about the child birth visit record of a patient.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class ChildBirthVisitBean implements Comparable<ChildBirthVisitBean>
{
	private long visitID = 0;
	private long obstetricsInitRecordID = 0;
	private long apptID = 0;
	private DeliveryType preferredDeliveryMethod = DeliveryType.NotSpecified;
	private BooleanType hasDelivered = BooleanType.NotSpecified;
	private long pitocinDosage = 0;
	private long nitrousOxideDosage = 0;
	private long pethidineDosage = 0;
	private long epiduralAnaesthesiaDosage = 0;
	private long magnesiumSulfateDosage = 0;
	private long rhImmuneGlobulinDosage = 0;
	
	public long getVisitID()
	{
		return visitID;
	}

	public void setVisitID(long visitID)
	{
		this.visitID = visitID;
	}
	
	public long getObstetricsInitRecordID()
	{
		return obstetricsInitRecordID;
	}

	public void setObstetricsInitRecordID(long obstetricsInitRecordID)
	{
		this.obstetricsInitRecordID = obstetricsInitRecordID;
	}
	
	public long getApptID()
	{
		return apptID;
	}

	public void setApptID(long apptID)
	{
		this.apptID = apptID;
	}
	
	public DeliveryType getPreferredDeliveryMethod()
	{
		return preferredDeliveryMethod;
	}

	public void setPreferredDeliveryMethodStr(String preferredDeliveryMethod)
	{
		this.preferredDeliveryMethod = DeliveryType.parse(preferredDeliveryMethod);
	}

	public void setPreferredDeliveryMethod(DeliveryType preferredDeliveryMethod)
	{
		this.preferredDeliveryMethod = preferredDeliveryMethod;
	}
	
	public BooleanType getHasDelivered()
	{
		return hasDelivered;
	}

	public void setHasDeliveredStr(String hasDelivered)
	{
		this.hasDelivered = BooleanType.parse(hasDelivered);
	}

	public void setHasDelivered(BooleanType hasDelivered)
	{
		this.hasDelivered = hasDelivered;
	}
	
	public long getPitocinDosage()
	{
		return pitocinDosage;
	}

	public void setPitocinDosage(long pitocinDosage)
	{
		this.pitocinDosage = pitocinDosage;
	}

	public long getPethidineDosage() {
		return pethidineDosage;
	}

	public void setPethidineDosage(long pethidineDosage) {
		this.pethidineDosage = pethidineDosage;
	}

	public long getNitrousOxideDosage()
	{
		return nitrousOxideDosage;
	}

	public void setNitrousOxideDosage(long nitrousOxideDosage)
	{
		this.nitrousOxideDosage = nitrousOxideDosage;
	}
	
	public long getEpiduralAnaesthesiaDosage()
	{
		return epiduralAnaesthesiaDosage;
	}

	public void setEpiduralAnaesthesiaDosage(long epiduralAnaesthesiaDosage)
	{
		this.epiduralAnaesthesiaDosage = epiduralAnaesthesiaDosage;
	}
	
	public long getMagnesiumSulfateDosage()
	{
		return magnesiumSulfateDosage;
	}

	public void setMagnesiumSulfateDosage(long magnesiumSulfateDosage)
	{
		this.magnesiumSulfateDosage = magnesiumSulfateDosage;
	}
	
	public long getRhImmuneGlobulinDosage()
	{
		return rhImmuneGlobulinDosage;
	}

	public void setRhImmuneGlobulinDosage(long rhImmuneGlobulinDosage)
	{
		this.rhImmuneGlobulinDosage = rhImmuneGlobulinDosage;
	}
	
	@Override
	public int compareTo(ChildBirthVisitBean o)
	{
		return (int)(o.visitID - this.visitID);
	}
	
	public int equals(ChildBirthVisitBean o)
	{
		return (int)(o.visitID - this.visitID);
	}
	
	@Override
	public int hashCode()
	{
		assert false : "hashCode not designed";
		return 42;
	}

	@Override
	public String toString() {
		return String.format("{\"prefDeliveryMethod\":\"%s\", \"hasDelivered\":\"%s\", \"pnd\":\"%d\", " +
						"\"nod\": \"%d\", \"ped\": \"%d\",  \"ead\":\"%d\", \"msd\": \"%d\", \"rgd\": \"%d\"," +
						" \"obstetricRecordID\": \"%d\", \"apptID\": \"%d\", \"visitID\": \"%d\"}",
				this.getPreferredDeliveryMethod().getName(),
				this.hasDelivered.getName(),
				this.getPitocinDosage(),
				this.getNitrousOxideDosage(),
				this.getPethidineDosage(),
				this.getEpiduralAnaesthesiaDosage(),
				this.getMagnesiumSulfateDosage(),
				this.getRhImmuneGlobulinDosage(),
				this.getObstetricsInitRecordID(),
				this.getApptID(),
				this.getVisitID());
	}
}
