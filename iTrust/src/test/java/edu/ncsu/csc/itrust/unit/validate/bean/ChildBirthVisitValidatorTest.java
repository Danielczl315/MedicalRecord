package edu.ncsu.csc.itrust.unit.validate.bean;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.enums.BooleanType;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.validate.ChildBirthVisitValidator;
import edu.ncsu.csc.itrust.model.old.validate.ValidationFormat;
import edu.ncsu.csc.itrust.exception.FormValidationException;

public class ChildBirthVisitValidatorTest extends TestCase {
	ChildBirthVisitValidator val;
	
	@Override
	protected void setUp() {
		val = new ChildBirthVisitValidator();
	}
	
	public void testCBVAllCorrect() {
		ChildBirthVisitBean cbv = new ChildBirthVisitBean();
		
		cbv.setVisitID(1);
		cbv.setObstetricsInitRecordID(1);
		cbv.setApptID(8);
		cbv.setPreferredDeliveryMethod(DeliveryType.VaginalDeliveryVacuumAssist);
		cbv.setHasDelivered(BooleanType.True);
		cbv.setPitocinDosage(10);
		cbv.setNitrousOxideDosage(10);
		cbv.setEpiduralAnaesthesiaDosage(5);
		cbv.setMagnesiumSulfateDosage(8);
		cbv.setRhImmuneGlobulinDosage(5);
		
		try {
			val.validate(cbv);
		}
		catch (FormValidationException e) {
			fail(e.getMessage());
		}
	}
	
	public void testCBVAllOver() {
		ChildBirthVisitBean cbv = new ChildBirthVisitBean();
		
		cbv.setObstetricsInitRecordID(111111111111L);
		cbv.setApptID(111111111111L);
		cbv.setPreferredDeliveryMethod(DeliveryType.VaginalDeliveryVacuumAssist);
		cbv.setHasDelivered(BooleanType.True);
		cbv.setPitocinDosage(100);
		cbv.setNitrousOxideDosage(100);
		cbv.setEpiduralAnaesthesiaDosage(100);
		cbv.setMagnesiumSulfateDosage(100);
		cbv.setRhImmuneGlobulinDosage(100);
		
		try {
			val.validate(cbv);
			fail("FormValidationException should have occurred");
		}
		catch (FormValidationException e) {
			assertEquals("Obstetrics Initialization Record ID: " + ValidationFormat.RECORD_ID.getDescription(), e.getErrorList().get(0));
			assertEquals("Appointment ID: " + ValidationFormat.APPT_ID.getDescription(), e.getErrorList().get(1));
			assertEquals("Pitocin Dosage: " + ValidationFormat.DOSAGE.getDescription(), e.getErrorList().get(2));
			assertEquals("Nitrous Oxide Dosage: " + ValidationFormat.DOSAGE.getDescription(), e.getErrorList().get(3));
			assertEquals("Epidural Anaesthesia Dosage: " + ValidationFormat.DOSAGE.getDescription(), e.getErrorList().get(4));
			assertEquals("Magnesium Sulfate Dosage: " + ValidationFormat.DOSAGE.getDescription(), e.getErrorList().get(5));
			assertEquals("RH Immune Globulin Dosage: " + ValidationFormat.DOSAGE.getDescription(), e.getErrorList().get(6));
		}
	}
	
	public void testCBVAllNegative() {
		ChildBirthVisitBean cbv = new ChildBirthVisitBean();
		
		cbv.setObstetricsInitRecordID(-1);
		cbv.setApptID(-8);
		cbv.setPreferredDeliveryMethod(DeliveryType.VaginalDeliveryVacuumAssist);
		cbv.setHasDelivered(BooleanType.True);
		cbv.setPitocinDosage(-10);
		cbv.setNitrousOxideDosage(-10);
		cbv.setEpiduralAnaesthesiaDosage(-5);
		cbv.setMagnesiumSulfateDosage(-8);
		cbv.setRhImmuneGlobulinDosage(-5);
		
		try {
			val.validate(cbv);
			fail("FormValidationException should have occurred");
		}
		catch (FormValidationException e) {
			assertEquals("Obstetrics Initialization Record ID: " + ValidationFormat.RECORD_ID.getDescription(), e.getErrorList().get(0));
			assertEquals("Appointment ID: " + ValidationFormat.APPT_ID.getDescription(), e.getErrorList().get(1));
			assertEquals("Pitocin Dosage: " + ValidationFormat.DOSAGE.getDescription(), e.getErrorList().get(2));
			assertEquals("Nitrous Oxide Dosage: " + ValidationFormat.DOSAGE.getDescription(), e.getErrorList().get(3));
			assertEquals("Epidural Anaesthesia Dosage: " + ValidationFormat.DOSAGE.getDescription(), e.getErrorList().get(4));
			assertEquals("Magnesium Sulfate Dosage: " + ValidationFormat.DOSAGE.getDescription(), e.getErrorList().get(5));
			assertEquals("RH Immune Globulin Dosage: " + ValidationFormat.DOSAGE.getDescription(), e.getErrorList().get(6));
		}
	}
	
	public void testCBVEnumsNotSet() {
		ChildBirthVisitBean cbv = new ChildBirthVisitBean();
		
		cbv.setObstetricsInitRecordID(1);
		cbv.setApptID(8);
		cbv.setPreferredDeliveryMethod(DeliveryType.NotSpecified);
		cbv.setHasDelivered(BooleanType.NotSpecified);
		cbv.setPitocinDosage(10);
		cbv.setNitrousOxideDosage(10);
		cbv.setEpiduralAnaesthesiaDosage(5);
		cbv.setMagnesiumSulfateDosage(8);
		cbv.setRhImmuneGlobulinDosage(5);
		
		try {
			val.validate(cbv);
			fail("FormValidationException should have occurred");
		}
		catch (FormValidationException e) {
			assertEquals("Preferred Delivery Method is not specified.", e.getErrorList().get(0));
		}
	}
	
	public void testCBVDrugsNotSet() {
		ChildBirthVisitBean cbv = new ChildBirthVisitBean();
		
		cbv.setObstetricsInitRecordID(1);
		cbv.setApptID(8);
		cbv.setPreferredDeliveryMethod(DeliveryType.VaginalDeliveryVacuumAssist);
		cbv.setHasDelivered(BooleanType.True);
		
		try {
			val.validate(cbv);
		}
		catch (FormValidationException e) {
			fail("FormValidationError should not have occurred");
		}
	}
}
