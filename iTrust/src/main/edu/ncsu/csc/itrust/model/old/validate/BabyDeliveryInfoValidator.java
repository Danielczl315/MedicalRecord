package edu.ncsu.csc.itrust.model.old.validate;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean;
import edu.ncsu.csc.itrust.model.old.enums.Gender;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.BooleanType;

/**
 * Validates a BabyDeliveryInfoBean
 */
public class BabyDeliveryInfoValidator extends BeanValidator<BabyDeliveryInfoBean>
{
	/**
	 * The default constructor.
	 */
	public BabyDeliveryInfoValidator()
	{
	}

	@Override
	public void validate(BabyDeliveryInfoBean bd) throws FormValidationException
	{
		ErrorList errorList = new ErrorList();
		
		errorList.addIfNotNull(checkFormat("Child Birth Visit ID", bd.getChildBirthVisitID(), ValidationFormat.VISIT_ID, false));
		errorList.addIfNotNull(checkFormat("Date of Birth", bd.getDateOfBirthStr(), ValidationFormat.DATE, false));
		
		if (bd.getGender() == Gender.NotSpecified)
		{
			errorList.addIfNotNull("Gender is not specified.");
		}
		
		if (bd.getDeliveryMethod() == DeliveryType.NotSpecified)
		{
			errorList.addIfNotNull("Delivery Method is not specified.");
		}
		
		if (bd.getIsEstimated() == BooleanType.NotSpecified)
		{
			errorList.addIfNotNull("Estimation Status is not specified.");
		}
		
		if (errorList.hasErrors())
		{
			throw new FormValidationException(errorList);
		}
	}
}
