package edu.ncsu.csc.itrust.unit.bean;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.model.old.beans.PriorPregnancyBean;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;

public class PriorPregnancyBeanTest extends TestCase {
	
	public void testBeanValid() {
		PriorPregnancyBean pp = new PriorPregnancyBean();
		pp.setPriorPregnancyID(1);
		pp.setObstetricRecordID(1);
		pp.setYearOfConception(2001);
		pp.setDaysPregnant(200);
		pp.setHoursInLabor(12.25f);
		pp.setWeightGain(5.6f);
		pp.setDeliveryType(DeliveryType.VaginalDeliveryVacuumAssist);
		pp.setMultiplicity(3);
		assertEquals(1, pp.getPriorPregnancyID());
		assertEquals(1, pp.getObstetricRecordID());
		assertEquals(2001, pp.getYearOfConception());
		assertEquals(200, pp.getDaysPregnant());
		assertEquals("28 weeks, 4 days", pp.getWeeksDaysPregnant());
		assertEquals(12.25f, pp.getHoursInLabor());
		assertEquals(5.6f, pp.getWeightGain());
		assertEquals(DeliveryType.VaginalDeliveryVacuumAssist, pp.getDeliveryType());
		assertEquals(3, pp.getMultiplicity());
		assertEquals(0, pp.compareTo(pp));
	}
	
	public void testSetDeliveryTypeStr() {
		PriorPregnancyBean pp = new PriorPregnancyBean();
		pp.setDeliveryTypeStr("Vaginal Delivery");
		
		assertEquals(DeliveryType.VaginalDelivery, pp.getDeliveryType());
	}
	
	public void testWeekDaysPregnant() {
		PriorPregnancyBean pp = new PriorPregnancyBean();
		pp.setWeeksDaysPregnant("2-6");
		
		assertEquals("2 weeks, 6 days", pp.getWeeksDaysPregnant());
		assertEquals(20, pp.getDaysPregnant());
	}
	
	public void testToString() {
		PriorPregnancyBean pp = new PriorPregnancyBean();
		pp.setPriorPregnancyID(1);
		pp.setObstetricRecordID(1);
		pp.setYearOfConception(2001);
		pp.setDaysPregnant(200);
		pp.setHoursInLabor(12.25f);
		pp.setWeightGain(5.6f);
		pp.setDeliveryType(DeliveryType.VaginalDeliveryVacuumAssist);
		pp.setMultiplicity(3);
		
		String fs = String.format("{\"yearOfConception\":\"%d\", \"daysPregnant\":\"%d\", \"hoursInLabor\":\"%.2f\", \"weightGain\": \"%.2f\", \"deliveryType\":\"%s\", \"multiplicity\": \"%d\", \"recordID\": \"%d\"}",
			pp.getYearOfConception(),
			pp.getDaysPregnant(),
			pp.getHoursInLabor(),
			pp.getWeightGain(),
			pp.getDeliveryType().toString(),
			pp.getMultiplicity(),
			pp.getPriorPregnancyID());
		
		assertEquals(fs, pp.toString());
	}
}
