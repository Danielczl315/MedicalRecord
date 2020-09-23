package edu.ncsu.csc.itrust.unit.validate.bean;

import junit.framework.TestCase;

import edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean;
import edu.ncsu.csc.itrust.model.old.validate.BabyDeliveryInfoValidator;
import edu.ncsu.csc.itrust.model.old.validate.ValidationFormat;
import edu.ncsu.csc.itrust.exception.FormValidationException;

public class BabyDeliveryInfoValidatorTest extends TestCase {
	private BabyDeliveryInfoValidator val;
	
	@Override
	protected void setUp() {
		val = new BabyDeliveryInfoValidator();
	}
	
	public void testBDIAllCorrect() {
		BabyDeliveryInfoBean bdi = new BabyDeliveryInfoBean();
		
		bdi.setRecordID(1);
		bdi.setChildBirthVisitID(1);
		bdi.setGenderStr("Male");
		bdi.setDateOfBirthStr("11/03/2001");
		bdi.setDeliveryMethodStr("Vaginal Delivery Vacuum Assist");
		bdi.setIsEstimatedStr("False");
		
		try {
			val.validate(bdi);
		}
		catch (FormValidationException e) {
			fail(e.getMessage());
		}
	}
	
	public void testBDIAllInvalid() {
		BabyDeliveryInfoBean bdi = new BabyDeliveryInfoBean();
		
		bdi.setChildBirthVisitID(-1);
		bdi.setGenderStr("Not Specified");
		bdi.setDateOfBirthStr("ll/OE/200L");
		bdi.setDeliveryMethodStr("Not Specified");
		bdi.setIsEstimatedStr("Not Specified");
		
		try {
			val.validate(bdi);
			fail("FormValidationException should have occurred");
		}
		catch (FormValidationException e) {
			assertEquals("Child Birth Visit ID: " + ValidationFormat.VISIT_ID.getDescription(), e.getErrorList().get(0));
			assertEquals("Gender is not specified.", e.getErrorList().get(1));
			assertEquals("Delivery Method is not specified.", e.getErrorList().get(2));
			assertEquals("Estimation Status is not specified.", e.getErrorList().get(3));
		}
	}
}
