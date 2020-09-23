package edu.ncsu.csc.itrust.unit.bean;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.enums.BooleanType;
import edu.ncsu.csc.itrust.model.old.enums.DeliveryType;

public class ChildBirthVisitBeanTest extends TestCase {
	public void testBean() throws Exception {
		ChildBirthVisitBean cbv = new ChildBirthVisitBean();
		
		cbv.setVisitID(1);
		cbv.setObstetricsInitRecordID(1);
		cbv.setApptID(8);
		cbv.setPreferredDeliveryMethod(DeliveryType.VaginalDeliveryVacuumAssist);
		cbv.setHasDelivered(BooleanType.True);
		cbv.setPethidineDosage(10);
		cbv.setPitocinDosage(10);
		cbv.setNitrousOxideDosage(10);
		cbv.setEpiduralAnaesthesiaDosage(5);
		cbv.setMagnesiumSulfateDosage(8);
		cbv.setRhImmuneGlobulinDosage(5);
		
		assertEquals(1, cbv.getVisitID());
		assertEquals(1, cbv.getObstetricsInitRecordID());
		assertEquals(8, cbv.getApptID());
		assertEquals(DeliveryType.VaginalDeliveryVacuumAssist, cbv.getPreferredDeliveryMethod());
		assertEquals(BooleanType.True, cbv.getHasDelivered());
		assertEquals(10, cbv.getPethidineDosage());
		assertEquals(10, cbv.getPitocinDosage());
		assertEquals(10, cbv.getNitrousOxideDosage());
		assertEquals(5, cbv.getEpiduralAnaesthesiaDosage());
		assertEquals(8, cbv.getMagnesiumSulfateDosage());
		assertEquals(5, cbv.getRhImmuneGlobulinDosage());
		assertEquals(0, cbv.compareTo(cbv));
		assertEquals(0, cbv.equals(cbv));
		
		cbv.setPreferredDeliveryMethodStr("Vaginal Delivery Vacuum Assist");
		assertEquals(DeliveryType.VaginalDeliveryVacuumAssist, cbv.getPreferredDeliveryMethod());
		
		cbv.setHasDeliveredStr("True");
		assertEquals(BooleanType.True, cbv.getHasDelivered());
		
		assertEquals("{\"prefDeliveryMethod\":\"Vaginal Delivery Vacuum Assist\", \"hasDelivered\":\"True\", \"pnd\":\"10\", " +
				"\"nod\": \"10\", \"ped\": \"10\",  \"ead\":\"5\", \"msd\": \"8\", \"rgd\": \"5\"," +
				" \"obstetricRecordID\": \"1\", \"apptID\": \"8\", \"visitID\": \"1\"}",
				cbv.toString());
	}
}
