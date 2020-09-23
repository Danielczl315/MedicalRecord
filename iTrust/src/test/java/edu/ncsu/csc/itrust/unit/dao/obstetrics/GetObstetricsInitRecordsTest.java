package edu.ncsu.csc.itrust.unit.dao.obstetrics;

import edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsInitDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.util.List;

public class GetObstetricsInitRecordsTest extends TestCase {
	private TestDataGenerator gen = new TestDataGenerator();
	private ObstetricsInitDAO oiDAO = TestDAOFactory.getTestInstance().getObstetricsInitDAO();
	
	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
	}
	
	public void testGetObstetricsRecordByMID() throws Exception {
		ObstetricsInitBean oi1;
		ObstetricsInitBean oi2;
		
		gen.obstetricsInit1();
		List<ObstetricsInitBean> oiList = oiDAO.getObstetricsInitByMID(1L);
		oi1 = oiList.get(0);
		oi2 = oiList.get(1);
		
		assertEquals(1, oi1.getRecordID());
		assertEquals(1, oi1.getPatientMID());
		assertEquals("01/30/2001", oi1.getInitDateMMDDYYYY());
		assertEquals("12/25/2000", oi1.getLastMenstrualPeriodMMDDYYYY());
		
		assertEquals(2, oi2.getRecordID());
		assertEquals(1, oi2.getPatientMID());
		assertEquals("03/20/2004", oi2.getInitDateMMDDYYYY());
		assertEquals("02/22/2004", oi2.getLastMenstrualPeriodMMDDYYYY());
	}
	
	public void testGetObstetricsRecordByID() throws Exception {
		ObstetricsInitBean oi;
		
		gen.obstetricsInit1();
		oi = oiDAO.getObstetricsInitByID(2L);
		assertEquals(2, oi.getRecordID());
		assertEquals(1, oi.getPatientMID());
		assertEquals("03/20/2004", oi.getInitDateMMDDYYYY());
		assertEquals("02/22/2004", oi.getLastMenstrualPeriodMMDDYYYY());
	}
	
	public void testGetObstetricsRecordByMostRecent() throws Exception {
		ObstetricsInitBean oi;
		
		gen.obstetricsInit1();
		oi = oiDAO.getObstetricsInitByMostRecent(1);
		
		oi = oiDAO.getObstetricsInitByID(2L);
		assertEquals(2, oi.getRecordID());
		assertEquals(1, oi.getPatientMID());
		assertEquals("03/20/2004", oi.getInitDateMMDDYYYY());
		assertEquals("02/22/2004", oi.getLastMenstrualPeriodMMDDYYYY());
	}
	
	public void testDeleteObstetricsInitRecord() throws Exception {
		gen.obstetricsInit1();
		
		ObstetricsInitBean oi;
		oiDAO.deleteObstetricInit(1);
		oi = oiDAO.getObstetricsInitByID(1);
		
		assertEquals(null, oi);
	}
}
