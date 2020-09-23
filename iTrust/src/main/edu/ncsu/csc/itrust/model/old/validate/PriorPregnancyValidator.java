package edu.ncsu.csc.itrust.model.old.validate;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.PriorPregnancyBean;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;

/**
 * Validates a PriorPregnancyBean
 */
public class PriorPregnancyValidator extends BeanValidator<PriorPregnancyBean>
{
	/**
	 * The default constructor.
	 */
	public PriorPregnancyValidator()
	{
	}

	@Override
	public void validate(PriorPregnancyBean pp) throws FormValidationException
	{
		ErrorList errorList = new ErrorList();
		
		errorList.addIfNotNull(checkFormat("OB Record ID", pp.getObstetricRecordID(), ValidationFormat.RECORD_ID, false));
		errorList.addIfNotNull(checkFormat("Year of Conception", pp.getYearOfConception(), ValidationFormat.YEAR, false));
		errorList.addIfNotNull(checkFormat("Weeks and Days Pregnant", pp.getWeeksDaysPregnant(), ValidationFormat.WEEKS_PREGNANT, false));
		errorList.addIfNotNull(checkFormat("Hours in Labor", Float.toString(pp.getHoursInLabor()), ValidationFormat.HOURS_LABOR, false));
		errorList.addIfNotNull(checkFormat("Weight Gain", Float.toString(pp.getWeightGain()), ValidationFormat.WEIGHT, false));
		errorList.addIfNotNull(checkFormat("Multiplicity", pp.getMultiplicity(), ValidationFormat.MULTIPLICITY, false));
		
		if (pp.getDeliveryType() == DeliveryType.Miscarriage && pp.getMultiplicity() > 0)
		{
			errorList.addIfNotNull("Multiplicity cannot be greater than zero when miscarriage.");
		}
		
		if (errorList.hasErrors())
		{
			throw new FormValidationException(errorList);
		}
	}
}
