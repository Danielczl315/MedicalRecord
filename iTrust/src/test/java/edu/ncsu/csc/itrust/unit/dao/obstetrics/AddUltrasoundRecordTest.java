package edu.ncsu.csc.itrust.unit.dao.obstetrics;

import junit.framework.TestCase;

import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.UltrasoundRecordDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

public class AddUltrasoundRecordTest extends TestCase {
	private TestDataGenerator gen = new TestDataGenerator();
	private UltrasoundRecordDAO usrDAO = TestDAOFactory.getTestInstance().getUltrasoundRecordDAO();
	
	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
	}
	
	public void testAddEmptyUltrasoundRecord() throws Exception {
		long recordID = usrDAO.addEmptyUltrasoundRecord();
		assertEquals(true, usrDAO.checkUltrasoundRecordExistsByRecordID(recordID));
		assertEquals(true, usrDAO.checkUltrasoundRecordExistsByVisitID(0));
	}
	
	/* Tests editUltrasoundRecord as well */
	public void testAddUltrasoundRecord() throws Exception {
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
		
		usrDAO.addUltrasoundRecord(usr);
		usr = usrDAO.getUltrasoundRecordByRecordID(1);
		
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
	}
	
	public void testAddUltrasoundRecordByRecord() throws Exception {
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
		
		usrDAO.addUltrasoundRecord(usr);
		usr = usrDAO.getUltrasoundRecordByVisitID(1).get(0);
		
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
	}
	
	public void testDelete() throws Exception {
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
		
		usrDAO.addUltrasoundRecord(usr);
		
		assertEquals(true, usrDAO.deleteUltrasoundRecord(1));
	}
}
