package edu.ncsu.csc.itrust.model.old.validate;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;

/**
 * Validates a ChildBirthVisitBean
 */
public class ChildBirthVisitValidator extends BeanValidator<ChildBirthVisitBean>
{
	/**
	 * The default constructor.
	 */
	public ChildBirthVisitValidator()
	{
	}

	@Override
	public void validate(ChildBirthVisitBean cb) throws FormValidationException
	{
		ErrorList errorList = new ErrorList();
		
		errorList.addIfNotNull(checkFormat("Obstetrics Initialization Record ID", cb.getObstetricsInitRecordID(), ValidationFormat.RECORD_ID, false));
		errorList.addIfNotNull(checkFormat("Appointment ID", cb.getApptID(), ValidationFormat.APPT_ID, false));
		errorList.addIfNotNull(checkFormat("Pitocin Dosage", cb.getPitocinDosage(), ValidationFormat.DOSAGE, false));
		errorList.addIfNotNull(checkFormat("Nitrous Oxide Dosage", cb.getNitrousOxideDosage(), ValidationFormat.DOSAGE, false));
		errorList.addIfNotNull(checkFormat("Epidural Anaesthesia Dosage", cb.getEpiduralAnaesthesiaDosage(), ValidationFormat.DOSAGE, false));
		errorList.addIfNotNull(checkFormat("Magnesium Sulfate Dosage", cb.getMagnesiumSulfateDosage(), ValidationFormat.DOSAGE, false));
		errorList.addIfNotNull(checkFormat("RH Immune Globulin Dosage", cb.getRhImmuneGlobulinDosage(), ValidationFormat.DOSAGE, false));
		
		if (cb.getPreferredDeliveryMethod() == DeliveryType.NotSpecified)
		{
			errorList.addIfNotNull("Preferred Delivery Method is not specified.");
		}
		
		if (errorList.hasErrors())
		{
			throw new FormValidationException(errorList);
		}
	}
}
