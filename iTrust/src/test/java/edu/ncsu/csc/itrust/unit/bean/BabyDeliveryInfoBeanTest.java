package edu.ncsu.csc.itrust.unit.bean;

import junit.framework.TestCase;
import java.text.SimpleDateFormat;
import edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean;
import edu.ncsu.csc.itrust.model.old.enums.BooleanType;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;
import edu.ncsu.csc.itrust.model.old.enums.Gender;

public class BabyDeliveryInfoBeanTest extends TestCase {
	private SimpleDateFormat formatter;
	
	@Override
	protected void setUp() {
		formatter = new SimpleDateFormat("MM/dd/yyyy");
	}
	
	public void testBean() throws Exception {
		BabyDeliveryInfoBean bdi = new BabyDeliveryInfoBean();
		
		bdi.setRecordID(1);
		bdi.setChildBirthVisitID(1);
		bdi.setGenderStr("Male");
		bdi.setDateOfBirthStr("11/03/2001");
		bdi.setDeliveryMethodStr("Vaginal Delivery Vacuum Assist");
		bdi.setIsEstimatedStr("False");
		
		assertEquals(1, bdi.getRecordID());
		assertEquals(1, bdi.getChildBirthVisitID());
		assertEquals(Gender.Male, bdi.getGender());
		assertEquals(formatter.parse("11/03/2001"), bdi.getDateOfBirth());
		assertEquals("11/03/2001", bdi.getDateOfBirthStr());
		assertEquals(DeliveryType.VaginalDeliveryVacuumAssist, bdi.getDeliveryMethod());
		assertEquals(BooleanType.False, bdi.getIsEstimated());
		assertEquals(0, bdi.compareTo(bdi));
		assertEquals(0, bdi.equals(bdi));
		
		bdi.setGender(Gender.Female);
		assertEquals(Gender.Female, bdi.getGender());
		
		bdi.setDateOfBirthStr("11/aa/2001");
		bdi.setDateOfBirthStr("2001/11/03", "yyyy/MM/dd");
		assertEquals("11/03/2001", bdi.getDateOfBirthStr());
		
		bdi.setDateOfBirthStr("11/aa/2001", "yyyy/MM/dd");
		bdi.setDateOfBirthStr("11/03/2001");
		assertEquals("2001/11/03", bdi.getBirthDatePattern("yyyy/MM/dd"));
		
		bdi.setDeliveryMethod(DeliveryType.VaginalDeliveryForcepsAssist);
		assertEquals(DeliveryType.VaginalDeliveryForcepsAssist, bdi.getDeliveryMethod());
		
		bdi.setIsEstimated(BooleanType.True);
		assertEquals(BooleanType.True, bdi.getIsEstimated());
		
		assertEquals("{\"recordID\":\"1\", \"birthDate\":\"11/03/2001 00:00 AM\", \"deliveryType\":\"Vaginal Delivery Forceps Assist\", " +
				"\"sexType\": \"Female\", \"birthDateInput\": \"2001-11-03T00:00\"}", bdi.toString());
	}
}
