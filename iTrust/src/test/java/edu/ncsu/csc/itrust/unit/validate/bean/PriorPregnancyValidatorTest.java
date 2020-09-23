package edu.ncsu.csc.itrust.unit.validate.bean;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.model.old.beans.PriorPregnancyBean;
import edu.ncsu.csc.itrust.model.old.validate.PriorPregnancyValidator;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.validate.ValidationFormat;
import edu.ncsu.csc.itrust.exception.FormValidationException;

public class PriorPregnancyValidatorTest extends TestCase {
	PriorPregnancyValidator val;
	
	@Override
	protected void setUp() throws Exception {
		val = new PriorPregnancyValidator();
	}
	
	public void testPriorPregnancyAllCorrect() throws Exception {
		PriorPregnancyBean pp = new PriorPregnancyBean();
		
		pp.setObstetricRecordID(1);
		pp.setYearOfConception(2001);
		pp.setDaysPregnant(200);
		pp.setHoursInLabor(12.25f);
		pp.setWeightGain(5.6f);
		pp.setDeliveryType(DeliveryType.VaginalDeliveryVacuumAssist);
		pp.setMultiplicity(3);
		val.validate(pp);
	}
	
	public void testPriorPregnancyAllOver() {
		PriorPregnancyBean pp = new PriorPregnancyBean();
		
		pp.setObstetricRecordID(1);
		pp.setYearOfConception(20650);
		pp.setDaysPregnant(301);	/* 43 weeks */
		pp.setHoursInLabor(1000.0f);
		pp.setWeightGain(10000.99f);
		pp.setMultiplicity(999);
		
		try {
			val.validate(pp);
			fail("FormValidationException should have been thrown");
		}
		catch (FormValidationException e) {
			assertEquals("OB Record ID: " + ValidationFormat.MID.getDescription(), e.getErrorList().get(0));
			assertEquals("Year of Conception: " + ValidationFormat.YEAR.getDescription(), e.getErrorList().get(1));
			assertEquals("Weeks and Days Pregnant: " + ValidationFormat.WEEKS_PREGNANT.getDescription(), e.getErrorList().get(2));
			assertEquals("Hours in Labor: " + ValidationFormat.HOURS_LABOR.getDescription(), e.getErrorList().get(3));
			assertEquals("Weight Gain: " + ValidationFormat.WEIGHT.getDescription(), e.getErrorList().get(4));
			assertEquals("Multiplicity: " + ValidationFormat.MULTIPLICITY.getDescription(), e.getErrorList().get(5));
		}
	}
	
	public void testPriorPregnancyAllNegative() {
		PriorPregnancyBean pp = new PriorPregnancyBean();
		
		pp.setObstetricRecordID(-1);
		pp.setYearOfConception(-1);
		pp.setDaysPregnant(-1);	/* 43 weeks */
		pp.setHoursInLabor(-1.0f);
		pp.setWeightGain(-1.0f);
		pp.setMultiplicity(-1);
		
		try {
			val.validate(pp);
			fail("FormValidationException should have been thrown");
		}
		catch (FormValidationException e) {
			assertEquals("Patient MID: " + ValidationFormat.MID.getDescription(), e.getErrorList().get(0));
			assertEquals("Year of Conception: " + ValidationFormat.YEAR.getDescription(), e.getErrorList().get(1));
			assertEquals("Weeks and Days Pregnant: " + ValidationFormat.WEEKS_PREGNANT.getDescription(), e.getErrorList().get(2));
			assertEquals("Hours in Labor: " + ValidationFormat.HOURS_LABOR.getDescription(), e.getErrorList().get(3));
			assertEquals("Weight Gain: " + ValidationFormat.WEIGHT.getDescription(), e.getErrorList().get(4));
			assertEquals("Multiplicity: " + ValidationFormat.MULTIPLICITY.getDescription(), e.getErrorList().get(5));
		}
	}
}
