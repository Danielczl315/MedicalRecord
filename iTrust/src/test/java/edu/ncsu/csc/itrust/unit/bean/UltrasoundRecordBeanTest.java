package edu.ncsu.csc.itrust.unit.bean;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;

public class UltrasoundRecordBeanTest extends TestCase {
	public void testBean() throws Exception {
		UltrasoundRecordBean usr = new UltrasoundRecordBean();
		usr.setRecordID(1);
		usr.setObstetricsOfficeVisitID(1);
		usr.setCrownRumpLength(3.4f);
		usr.setBiparietalDiameter(2.6f);
		usr.setHeadCircumference(3.1f);
		usr.setFemurLength(5.4f);
		usr.setOccipitofrontalDiameter(1.7f);
		usr.setAbdominalCircumference(4.9f);
		usr.setHumerusLength(4.5f);
		usr.setEstimatedFetalWeight(6.8f);
		usr.setFileURL("nonexistent");
		assertEquals(1, usr.getRecordID());
		assertEquals(1, usr.getObstetricsOfficeVisitID());
		assertEquals(3.4f, usr.getCrownRumpLength());
		assertEquals(2.6f, usr.getBiparietalDiameter());
		assertEquals(3.1f, usr.getHeadCircumference());
		assertEquals(5.4f, usr.getFemurLength());
		assertEquals(1.7f, usr.getOccipitofrontalDiameter());
		assertEquals(4.9f, usr.getAbdominalCircumference());
		assertEquals(4.5f, usr.getHumerusLength());
		assertEquals(6.8f, usr.getEstimatedFetalWeight());
		assertEquals("nonexistent", usr.getFileURL());
		assertEquals(0, usr.compareTo(usr));
		assertEquals(0, usr.equals(usr));
		assertEquals(" {\"recordID\":\"1\", \"crownRumpLength\":\"3.40\", \"bpDiameter\":\"2.60\", " +
					"\"headCircumference\": \"3.10\", \"femurLength\":\"5.40\", \"ofDiameter\": \"1.70\", " +
					"\"abCircumference\":\"4.90\", " +
					"\"humerusLength\":\"4.50\", " +
					"\"estFetalWeight\":\"6.80\", \"fileURL\":\"nonexistent\"} ",
					usr.toString());
	}
}
